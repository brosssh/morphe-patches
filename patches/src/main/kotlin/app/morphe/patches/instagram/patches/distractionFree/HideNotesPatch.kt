package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private object NotesTrayViewInitFingerprint : Fingerprint(
    name = "<init>",
    filters = listOf(
        string("NotesTray")
    ),
    parameters = listOf("L", "Landroid/view/View;", "L", "L", "L", "L", "L", "L", "L")
)

private object NoteInProfileViewInitFingerprint : Fingerprint(
    name = "<init>",
    definingClass = "/NoteBubbleView;",
    parameters = listOf("Landroid/content/Context;", "Landroid/util/AttributeSet;", "I")
)

private val hideNotesTrayPatch = bytecodePatch(
    description = "Hides the notes tray from the direct tab."
) {
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

private val hideNoteInProfilePatch = bytecodePatch(
    description = "Hides the note popup in the profile icon."
) {
    execute {
        NoteInProfileViewInitFingerprint.method.apply {
            val addViewIndex = indexOfFirstInstructionOrThrow {
                getReference<MethodReference>()?.name == "addView"
            }

            removeInstruction(addViewIndex)
        }
    }
}

@Suppress("unused")
val hideNotesPatch = bytecodePatch(
    name = "Hide notes",
    description = "Hides the notes tray from the direct tab and the notes popup in the profile icon.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
    dependsOn(hideNotesTrayPatch, hideNoteInProfilePatch)
}
