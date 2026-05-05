package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.intOption
import app.morphe.patcher.string
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.indexOfFirstInstructionOrThrow
import app.morphe.util.trimIndentMultiline
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private object InitializeProfileActionBarFingerprint : Fingerprint (
    filters = listOf(
        string("null cannot be cast to non-null type com.instagram.profile.ui.fadeinfollowbutton.FadeInFollowButton")
    ),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL, AccessFlags.STATIC)
)


@Suppress("unused")
val hideThreadsProfileButtonPatch = bytecodePatch(
    name = "Hide Threads profile button",
    description = """
        Hides the Threads button from the profile page action bar (top right of the profile page).
        The target button position can be changed via the "Button index" option if needed.
    """.trimIndentMultiline(),
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    val buttonIndex by intOption(
        key = "buttonIndex",
        default = 0,
        title = "Button index",
        description = """
            Position of the button to hide from the profile page action bar (zero-based).
            
            0 = first button (default, targets the Threads button on most accounts).
            1 = second button.
            2 = third button.
            
            If the Threads button does not disappear, try incrementing this value by 1.
        """.trimIndentMultiline(),
        required = true,
        validator = { it!! <= 2 }
    )

    execute {
        InitializeProfileActionBarFingerprint.method.apply {
            val returnVoidIndex = indexOfFirstInstructionOrThrow(Opcode.RETURN_VOID)

            addInstructionsAtControlFlowLabel(
                returnVoidIndex,
                """
                    move-object/from16 v0, p4
    
                    const/4 v1, 0x$buttonIndex
                    invoke-virtual {v0, v1}, Landroid/view/ViewGroup;->getChildAt(I)Landroid/view/View;
                    move-result-object v1
    
                    # Null-check
                    if-eqz v1, :cond_skip
                    invoke-virtual {v0, v1}, Landroid/view/ViewGroup;->removeView(Landroid/view/View;)V
                    
                    :cond_skip
                    return-void
                """)
        }
    }
}
