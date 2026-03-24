package app.morphe.patches.instagram.misc.share.privacy

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.extension.sharedExtensionPatch
import app.morphe.patches.instagram.misc.share.editShareLinksPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/misc/share/privacy/SanitizeSharingLinksPatch;"

@Suppress("unused")
val sanitizeSharingLinksPatch = bytecodePatch(
    name = "Sanitize sharing URLs",
    description = "Sanitize sharing URLs by removing tracking IDs",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(sharedExtensionPatch)

    execute {
        editShareLinksPatch { index, register ->
            addInstructions(
                index,
                """
                    invoke-static { v$register }, $EXTENSION_CLASS_DESCRIPTOR->sanitizeSharingLink(Ljava/lang/String;)Ljava/lang/String;
                    move-result-object v$register
                """
            )
        }
    }
}
