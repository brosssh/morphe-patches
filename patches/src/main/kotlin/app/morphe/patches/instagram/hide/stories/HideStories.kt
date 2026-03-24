package app.morphe.patches.instagram.hide.stories

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM

@Suppress("unused")
val hideStoriesPatch = bytecodePatch(
    name = "Hide Stories from Home",
    description = "Hides Stories from the main page, by removing the buttons.",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val addStoryEndIndex = getOrCreateAvatarViewFingerprint.instructionMatches.last().index

        // Remove addView of Story.
        getOrCreateAvatarViewFingerprint.method.removeInstruction(addStoryEndIndex)
    }
}
