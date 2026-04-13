package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

private object OnStoryTimeoutActionFingerprint : Fingerprint(
    parameters = listOf("Ljava/lang/Object;"),
    returnType = "V",
    strings = listOf("userSession"),
    definingClass = "/ReelViewerFragment;"
)

@Suppress("unused")
val disableStoryAutoFlippingPatch = bytecodePatch(
    name = "Disable story auto flipping",
    description = "Disable stories automatically flipping/skipping after some seconds.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        OnStoryTimeoutActionFingerprint.method.returnEarly()
    }
}
