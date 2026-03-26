package app.morphe.patches.instagram.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.shared.replaceJsonFieldWithBogus
import app.morphe.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide ads",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        AdInjectorFingerprint.method.returnEarly(false)

        ReelAdsFingerprint.matchAll().forEach {
            it.method.replaceJsonFieldWithBogus(REEL_ADS_KEY)
        }
    }
}
