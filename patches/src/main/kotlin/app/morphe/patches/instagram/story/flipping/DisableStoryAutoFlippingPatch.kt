package app.morphe.patches.instagram.story.flipping

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.SUPPORTED_INSTAGRAM_VERSION
import app.morphe.util.returnEarly

@Suppress("unused")
val disableStoryAutoFlippingPatch = bytecodePatch(
    name = "Disable story auto flipping",
    description = "Disable stories automatically flipping/skipping after some seconds.",
    use = false
) {
    compatibleWith(SUPPORTED_INSTAGRAM_VERSION)

    execute {
        onStoryTimeoutActionFingerprint.method.returnEarly()
    }
}
