package app.morphe.patches.instagram.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        adInjectorFingerprint.method.returnEarly(false)
    }
}
