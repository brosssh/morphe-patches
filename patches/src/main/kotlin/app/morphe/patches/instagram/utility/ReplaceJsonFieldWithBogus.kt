package app.morphe.patches.instagram.utility

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.indexOfFirstStringInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

/**
 * Replacing the JSON key we want to skip with a random string that is not a valid JSON key.
 * This way the feeds array will never be populated.
 * Received JSON keys that are not handled are simply ignored, so there are no side effects.
 */
context(_: BytecodePatchContext)
internal fun JsonParserMatch.replaceJsonFieldWithBogus() {
    method.apply {
        if (isStringMatch) {
            val targetStringRegister = getInstruction<OneRegisterInstruction>(matchIndex).registerA

            addInstruction(
                matchIndex + 1,
                "const-string v$targetStringRegister, \"BOGUS\"",
            )
        } else {
            val targetIntRegister = getInstruction<OneRegisterInstruction>(matchIndex).registerA

            addInstruction(
                matchIndex + 1,
                "const/4 v$targetIntRegister, 0x0",
            )
        }
    }
}

internal fun MutableMethod.replaceJsonFieldWithBogus(
    key: String
) {
    val targetStringIndex = indexOfFirstStringInstructionOrThrow(key)
    val targetStringRegister = getInstruction<OneRegisterInstruction>(targetStringIndex).registerA

    /**
     * Replacing the JSON key we want to skip with a random string that is not a valid JSON key.
     * This way the feeds array will never be populated.
     * Received JSON keys that are not handled are simply ignored, so there are no side effects.
     */
    replaceInstruction(
        targetStringIndex,
        "const-string v$targetStringRegister, \"BOGUS\"",
    )
}
