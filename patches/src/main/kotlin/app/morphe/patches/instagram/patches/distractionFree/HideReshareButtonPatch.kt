package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderSparseSwitchPayload
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.SparseSwitchPayload

// The hash code of the field of interest. It is used as the key of a hashmap
private val hashedFieldInteger = "enable_media_notes_production".hashCode()

private object FeedResponseMediaParserFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("array_out_of_bounds_exception", "null_pointer_exception", "MediaDict")
    ),
    custom = { method, _ ->
        method.indexOfFirstInstruction {
            opcode == Opcode.SPARSE_SWITCH_PAYLOAD &&
                    (this as SparseSwitchPayload).switchElements.any { it.key == hashedFieldInteger }
        } >= 0
    }
)

private object LiveTreeGetOptionalBooleanFingerprint : Fingerprint (
    name = "getOptionalBooleanValueByHashCode",
    definingClass = "Lcom/instagram/pando/livetree/LiveTreeJNI;"
)

@Suppress("unused")
val hideReshareButtonPatch = bytecodePatch(
    name = "Hide reshare button",
    description = "Hides the reshare button from both posts and reels.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        FeedResponseMediaParserFingerprint.method.apply {
            // Each json field is parsed in a switch statement, where the case of the switch is the hashed field name.

            // First, find the switch payload where our field of interest is being processed. So find the payload that
            // has a key == to our field of interest.
            val switchPayload = implementation!!.instructions.first { ins ->
                ins.opcode == Opcode.SPARSE_SWITCH_PAYLOAD &&
                        (ins as BuilderSparseSwitchPayload).switchElements.any { it.key == hashedFieldInteger }
            } as BuilderSparseSwitchPayload

            // Get the target label, so find the instruction offset where the switch case is pointing to.
            val switchTargetLabel = switchPayload.switchElements
                .first { it.key == hashedFieldInteger }
                .target

            // From that label, navigate forward until our field of interest is being instantiated.
            val moveResultIndex = indexOfFirstInstructionOrThrow(
                switchTargetLabel.location.index,
                Opcode.MOVE_RESULT_OBJECT
            )

            val moveResultRegister = getInstruction<OneRegisterInstruction>(moveResultIndex).registerA

            addInstructions(
                moveResultIndex + 1,
                " sget-object v$moveResultRegister, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;"
            )
        }

        // If it's trying to get the value for our field of interest via the Pando native library,
        // force the value to false instead
        LiveTreeGetOptionalBooleanFingerprint.method.addInstructions(
            0,
            """
                const v0, $hashedFieldInteger
                if-ne p1, v0, :nopatch
                sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object v0
                :nopatch
                nop
        """
        )
    }
}
