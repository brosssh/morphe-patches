package app.morphe.patches.instagram.story.flipping

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

@Suppress("unused")
val disableStoryAutoFlippingPatch = bytecodePatch(
    name = "Disable story auto flipping",
    description = "Disable stories automatically flipping/skipping after some seconds.",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        OnStoryTimeoutActionFingerprint.method.returnEarly()
    }
}
