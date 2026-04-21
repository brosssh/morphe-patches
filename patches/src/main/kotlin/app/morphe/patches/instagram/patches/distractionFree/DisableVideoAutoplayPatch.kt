package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

private object DisableVideoAutoPlayFingerprint : Fingerprint(
    strings = listOf("ig_disable_video_autoplay"),
    returnType = "Z"
)

@Suppress("unused")
val disableVideAutoplayPatch = bytecodePatch(
    name = "Disable video autoplay",
    description = "Disable video autoplay from feed",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        DisableVideoAutoPlayFingerprint.method.returnEarly(true)
    }
}
