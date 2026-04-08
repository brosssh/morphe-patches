package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import com.android.tools.smali.dexlib2.Opcode

private object GetOrCreateAvatarViewFingerprint : Fingerprint(
    name = "getOrCreateAvatarView",
    definingClass = "/RecyclerReelAvatarView;",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_VIRTUAL,
        Opcode.IPUT_OBJECT,
        Opcode.INVOKE_VIRTUAL // Add View (Story)
    )
)


@Suppress("unused")
val hideStoriesFromHomePatch = bytecodePatch(
    name = "Hide Stories from Home",
    description = "Hides Stories from the main page, by removing the buttons."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val addStoryEndIndex = GetOrCreateAvatarViewFingerprint.instructionMatches.last().index

        // Remove addView of Story.
        GetOrCreateAvatarViewFingerprint.method.removeInstruction(addStoryEndIndex)
    }
}
