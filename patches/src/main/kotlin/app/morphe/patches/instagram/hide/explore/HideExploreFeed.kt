package app.morphe.patches.instagram.hide.explore

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

context(BytecodePatchContext)
internal fun Fingerprint.replaceJsonFieldWithBogus(
    key: String,
) {
    val targetStringIndex = stringMatches.first { match -> match.string == key }.index
    val targetStringRegister = method.getInstruction<OneRegisterInstruction>(targetStringIndex).registerA

    /**
     * Replacing the JSON key we want to skip with a random string that is not a valid JSON key.
     * This way the feeds array will never be populated.
     * Received JSON keys that are not handled are simply ignored, so there are no side effects.
     */
    method.replaceInstruction(
        targetStringIndex,
        "const-string v$targetStringRegister, \"BOGUS\"",
    )
}

@Suppress("unused")
val hideExploreFeedPatch = bytecodePatch(
    name = "Hide explore feed",
    description = "Hides posts and reels from the explore/search page.",
    use = false,
) {
    compatibleWith("com.instagram.android"("422.0.0.0.35"))

    execute {
        exploreResponseJsonParserFingerprint.replaceJsonFieldWithBogus(EXPLORE_KEY_TO_BE_HIDDEN)
    }
}
