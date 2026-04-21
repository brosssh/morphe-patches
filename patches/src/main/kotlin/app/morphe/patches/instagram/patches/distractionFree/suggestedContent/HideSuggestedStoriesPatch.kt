package app.morphe.patches.instagram.patches.distractionFree.suggestedContent

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.findFreeRegister
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import app.morphe.util.indexOfFirstStringInstructionOrThrow
import app.morphe.util.registersUsed
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/stories/FilterStoriesPatch;"

private const val TRAY_JSON_PARSER_KEY = "tray"

// Enum is basically duplicated for unknown reason, the one we want has no Map fields
private object StoriesTypeEnumFingerprint : Fingerprint(
    strings = listOf("ads_reel", "user_reel"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    custom = { _, classDef ->
        !classDef.fields.any { it.type == "Ljava/util/Map;" }
    }
)

private object ReelResponseItemFingeprint : Fingerprint(
    definingClass = "/ReelResponseItem;"
)

private object StoriesTrayJsonParserFingerprint : Fingerprint(
    name = "unsafeParseFromJson",
    strings = listOf(TRAY_JSON_PARSER_KEY, "broadcasts")
)

val hideSuggestedStoriesPatch = bytecodePatch {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        StoriesTrayJsonParserFingerprint.method.apply {

            // Find the field name of the reel type
            val reelTypeField = ReelResponseItemFingeprint.classDef.fields.first {
                it.type == StoriesTypeEnumFingerprint.classDef.type
            }

            val trayStringIndex =
                indexOfFirstStringInstructionOrThrow(TRAY_JSON_PARSER_KEY)

            val setStoriesListIndex = indexOfFirstInstructionOrThrow(trayStringIndex) {
                opcode == Opcode.IPUT_OBJECT &&
                        getReference<FieldReference>()?.type == "Ljava/util/List;"
            }

            val setStoriesListRegisters = getInstruction(setStoriesListIndex).registersUsed
            val storiesListRegister = setStoriesListRegisters[0]

            val freeRegister = findFreeRegister(
                setStoriesListIndex,
                setStoriesListRegisters
            )

            addInstructionsAtControlFlowLabel(
                setStoriesListIndex,
                """
                    const-string v$freeRegister, "${reelTypeField.name}"
                    invoke-static { v$storiesListRegister, v$freeRegister }, $EXTENSION_CLASS_DESCRIPTOR->filterStories(Ljava/util/List;Ljava/lang/String;)Ljava/util/List;
                    move-result-object v$storiesListRegister
                """
            )
        }
    }
}
