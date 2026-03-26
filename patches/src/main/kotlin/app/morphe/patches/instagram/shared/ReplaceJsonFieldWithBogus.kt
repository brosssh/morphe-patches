package app.morphe.patches.instagram.shared

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.indexOfFirstStringInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

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
