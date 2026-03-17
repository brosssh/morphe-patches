package app.morphe.patches.instagram.story.flipping

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableStoryAutoFlippingPatch = bytecodePatch(
    name = "Disable story auto flipping",
    description = "Disable stories automatically flipping/skipping after some seconds.",
    use = false
) {
    compatibleWith("com.instagram.android"("422.0.0.0.35"))

    execute {
        onStoryTimeoutActionFingerprint.method.returnEarly()
    }
}
