package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

private object NotesTrayViewInitFingerprint : Fingerprint(
    name = "<init>",
    filters = listOf(
        string("NotesTray")
    ),
    parameters = listOf("L", "Landroid/view/View;", "L", "L", "L", "L", "L", "L", "L")
)

@Suppress("unused")
val hideNotesTrayPatch = bytecodePatch(
    name = "Hide notes tray",
    description = "Hide the notes tray from the direct tab.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        NotesTrayViewInitFingerprint.method.addInstructions(0, $$"""
            move-object/from16 v0, p2
            const/16 v1, 0x8
            invoke-virtual {v0, v1}, Landroid/view/View;->setVisibility(I)V
            
            move-object/from16 v0, p2
            const/4 v1, 0x0
            const/4 v2, 0x0
            new-instance v3, Landroid/view/ViewGroup$LayoutParams;
            invoke-direct {v3, v1, v2}, Landroid/view/ViewGroup$LayoutParams;-><init>(II)V
            invoke-virtual {v0, v3}, Landroid/view/View;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V
        """
        )
    }
}
