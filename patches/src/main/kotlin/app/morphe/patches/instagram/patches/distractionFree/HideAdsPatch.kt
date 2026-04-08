package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
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
    name = "Hide ads"
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        AdInjectorFingerprint.method.returnEarly(false)
    }
}
