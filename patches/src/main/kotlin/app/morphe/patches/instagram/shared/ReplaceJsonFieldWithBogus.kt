package app.morphe.patches.instagram.shared

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchContext
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
