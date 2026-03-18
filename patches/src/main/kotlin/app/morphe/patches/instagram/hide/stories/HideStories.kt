package app.morphe.patches.instagram.hide.stories

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.SUPPORTED_INSTAGRAM_VERSION

@Suppress("unused")
val hideStoriesPatch = bytecodePatch(
    name = "Hide Stories from Home",
    description = "Hides Stories from the main page, by removing the buttons.",
    use = false
) {
    compatibleWith(SUPPORTED_INSTAGRAM_VERSION)

    execute {
        val addStoryMethod = getOrCreateAvatarViewFingerprint.method // Creates Story
        val addStoryEndIndex = getOrCreateAvatarViewFingerprint.instructionMatches.lastIndex

        // Remove addView of Story.
        addStoryMethod.removeInstruction(addStoryEndIndex)
    }
}
