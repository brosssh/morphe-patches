package app.morphe.patches.alltrails.tracking

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patches.Constants
import java.util.logging.Logger

private fun MutableMethod.returnVoid() {
    addInstructions(0, "return-void")
}

private fun MutableMethod.returnTrue() {
    addInstructions(
        0,
        """
        const/4 v0, 0x1
        return v0
        """.trimIndent(),
    )
}

private fun MutableMethod.returnThis() {
    addInstructions(0, "return-object p0")
}

@Suppress("unused")
val disableTrackingPatch = bytecodePatch(
    name = "Disable tracking and analytics",
    description = "Disables non-essential third-party tracking and analytics SDKs " +
        "(Amplitude, AppsFlyer, Braze, Firebase Analytics, Mapbox telemetry) while leaving " +
        "core mapping, GPS recording, login, and sync intact. Firebase Crashlytics is left " +
        "enabled. Braze marketing push / in-app messages may stop working.",
    default = false,
) {
    compatibleWith(Constants.COMPATIBILITY_ALLTRAILS)

    execute {
        val logger = Logger.getLogger("DisableTrackingPatch")
        val blocked = mutableListOf<String>()

        fun tryBlock(sdk: String, fingerprintMethod: MutableMethod?, block: (MutableMethod) -> Unit) {
            if (fingerprintMethod == null) {
                logger.warning("Could not find $sdk hook; skipping")
                return
            }
            if (fingerprintMethod.implementation == null) {
                logger.warning("Skipping $sdk hook: method has no implementation (abstract/native)")
                return
            }
            try {
                block(fingerprintMethod)
                blocked.add(sdk)
            } catch (error: Exception) {
                logger.warning("Failed to apply $sdk hook: ${error.message}")
            }
        }

        tryBlock("AppsFlyer.isStopped", AppsFlyerIsStoppedFingerprint.methodOrNull) {
            it.returnTrue()
        }
        tryBlock("AppsFlyer.init", AppsFlyerInitFingerprint.methodOrNull) {
            it.returnThis()
        }

        tryBlock("Amplitude.logEvent", AmplitudeLogEventFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock("AmplitudeClient.logEvent", AmplitudeClientLogEventFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock("AmplitudeClient.initialize", AmplitudeClientInitializeFingerprint.methodOrNull) {
            it.returnThis()
        }

        tryBlock("FirebaseAnalytics.logEvent", FirebaseAnalyticsLogEventFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock(
            "FirebaseAnalytics.setAnalyticsCollectionEnabled",
            FirebaseAnalyticsSetCollectionEnabledFingerprint.methodOrNull,
        ) {
            it.returnVoid()
        }

        tryBlock("Braze.logCustomEvent", BrazeLogCustomEventFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock("Braze.openSession", BrazeOpenSessionFingerprint.methodOrNull) {
            it.returnVoid()
        }

        tryBlock("Mapbox TelemetryUtils.initialize", MapboxTelemetryInitializeFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock("Mapbox onAppUserTurnstileEvent", MapboxTurnstileFingerprint.methodOrNull) {
            it.returnVoid()
        }
        tryBlock("Mapbox sendMapLoadEvent", MapboxMapLoadEventFingerprint.methodOrNull) {
            it.returnVoid()
        }

        if (blocked.isEmpty()) {
            logger.warning("No tracking SDK hooks applied")
        } else {
            logger.info("Disabled tracking hooks: ${blocked.joinToString(", ")}")
        }
    }
}
