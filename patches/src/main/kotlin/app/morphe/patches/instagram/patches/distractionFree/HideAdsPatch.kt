package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.overrideMobileConfigBooleanFlag
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.AccessFlags

private object AdInjectorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE),
    returnType = "Z",
    parameters = listOf("L", "L"),
    strings = listOf("SponsoredContentController.insertItem")
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
        AdInjectorFingerprint.method.returnEarly(false)
    }
}
