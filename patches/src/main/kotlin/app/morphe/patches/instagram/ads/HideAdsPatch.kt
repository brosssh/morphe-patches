package app.morphe.patches.instagram.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.SUPPORTED_INSTAGRAM_VERSION
import app.morphe.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    use = false
) {
    compatibleWith(SUPPORTED_INSTAGRAM_VERSION)

    execute {
        adInjectorFingerprint.method.returnEarly(false)
    }
}
