package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patches.instagram.utility.JsonParserFingerprint
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.findFreeRegister
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/hide/suggestedContent/HideSuggestedStoriesPatch;"

private object ReelTypeEnumFingerprint : Fingerprint(
    name = "<clinit>",
    strings = listOf("ads_reel", "suggested_user", "suggested_user_reel")
)

private object ReelResponseItemFingerprint : Fingerprint (
    definingClass = "ReelResponseItem"
)

private object TrayFingerprint : JsonParserFingerprint(
    "tray",
    "hallpass_share_info"
)

context(_: BytecodePatchContext)
fun hideSuggestedStoriesPatch() = with(TrayFingerprint.match()) {
    method.apply {
        val storiesListAssignmentIndex = indexOfFirstInstructionOrThrow(matchIndex) {
            opcode == Opcode.IPUT_OBJECT && getReference<FieldReference>()?.type == "Ljava/util/List;"
        }

        val storiesListRegister =
            getInstruction<TwoRegisterInstruction>(storiesListAssignmentIndex).registerA

        val freeRegister = findFreeRegister(
            storiesListAssignmentIndex,
            storiesListRegister
        )

        val reelTypeFieldName = ReelResponseItemFingerprint.classDef.fields.first {
            ReelTypeEnumFingerprint.matchAll().map { it.classDef.type }.contains(it.type)
        }.name

        addInstructionsAtControlFlowLabel(
            storiesListAssignmentIndex,
            """
                        const-string v$freeRegister, "$reelTypeFieldName"
                        invoke-static { v$storiesListRegister, v$freeRegister }, $EXTENSION_CLASS_DESCRIPTOR->removeSuggestedStories(Ljava/util/List;Ljava/lang/String;)Ljava/util/List;
                        move-result-object v$storiesListRegister
                    """
        )
    }
}
