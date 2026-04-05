package app.morphe.patches.instagram.misc.removeBuildExpiredPopup

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val removeBuildExpiredPopupPatch = bytecodePatch(
    name = "Remove build expired popup",
    description = "Removes the popup that appears after a while, when the app version ages.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        AppUpdateLockoutBuilderFingerprint.method.apply {
            val longToIntIndex = instructions.first { it.opcode == Opcode.LONG_TO_INT }.location.index
            val appAgeRegister = getInstruction<TwoRegisterInstruction>(longToIntIndex).registerA

            // Set app age to 0 days old such that the build expired popup doesn't appear.
            addInstruction(longToIntIndex + 1, "const v$appAgeRegister, 0x0")
        }
    }
}
