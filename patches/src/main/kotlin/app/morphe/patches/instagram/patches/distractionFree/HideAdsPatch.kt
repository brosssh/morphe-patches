package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.patches.overrideMobileConfigBooleanFlag
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

private object DisableAdsFingerprint : Fingerprint(
    strings = listOf("Is ad pod"),
)

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    default = true
) {
    dependsOn(
        overrideMobileConfigBooleanFlag(
            override = "110800::0" to false // ig_android_controller_migration::use_v2_controller
        )
    )

    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        DisableAdsFingerprint.method.returnEarly(false)
    }
}
