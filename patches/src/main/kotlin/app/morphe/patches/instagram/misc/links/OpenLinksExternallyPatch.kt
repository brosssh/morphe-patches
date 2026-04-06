package app.morphe.patches.instagram.misc.links

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.extension.sharedExtensionPatch
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

private const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/instagram/misc/links/OpenLinksExternallyPatch;"

@Suppress("unused")
val openLinksExternallyPatch = bytecodePatch(
    name = "Open links externally",
    description = "Changes links to always open in your external browser, instead of the in-app browser.",
    default = false,
) {

    dependsOn(sharedExtensionPatch)

    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        InAppBrowserFunctionFingerprint.let {
            val stringMatchIndex = it.stringMatches.first { match -> match.string == TARGET_STRING }.index

            it.method.apply {
                val urlResultObjIndex = indexOfFirstInstructionOrThrow(
                    stringMatchIndex, Opcode.MOVE_OBJECT_FROM16
                )

                // Register that contains the url after moving from a higher register.
                val urlRegister = getInstruction<TwoRegisterInstruction>(urlResultObjIndex).registerA

                addInstructions(
                    urlResultObjIndex + 1,
                    """
                        invoke-static/range { v$urlRegister .. v$urlRegister }, $EXTENSION_CLASS_DESCRIPTOR->openExternally(Ljava/lang/String;)Z
                        move-result v0
                        return v0
                    """
                )
            }
        }
    }
}
