package app.morphe.patches.geocaching.subscription

import app.morphe.patches.shared.Constants
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium features",
    description = "WARNING: This only works via mount install (root required). " +
            "Unlock Premium features. Lists are still locked as they are server sided."
) {
    compatibleWith(Constants.COMPATIBILITY_GEOCACHING)

    execute {
        fun overrideRegister(fingerprint: Fingerprint, indexToOverride: Int) {
            with(fingerprint.method) {

                val createUserProfileIndex = indexOfFirstInstructionOrThrow {
                    opcode == Opcode.INVOKE_DIRECT_RANGE
                }

                val createUserStartingRegister =
                    getInstruction<RegisterRangeInstruction>(createUserProfileIndex).startRegister

                val registerToOverwrite = createUserStartingRegister + 1 + indexToOverride

                addInstruction(
                    createUserProfileIndex,
                    "const/16 v${registerToOverwrite}, 0x3"
                )
            }
        }

        overrideRegister(userProfileDeserializerFingerprint, 13)

        overrideRegister(ownProfileDeserializerFingerprint, 13)
    }
}
