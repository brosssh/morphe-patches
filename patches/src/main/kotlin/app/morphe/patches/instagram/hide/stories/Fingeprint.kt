package app.morphe.patches.instagram.hide.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

internal val getOrCreateAvatarViewFingerprint = Fingerprint(
    parameters = emptyList(),
    returnType = "L",
    definingClass = "Lcom/instagram/reels/ui/views/reelavatar/RecyclerReelAvatarView;",
    filters = listOf(
        opcode(Opcode.INVOKE_VIRTUAL),
        opcode(Opcode.IPUT_OBJECT),
        opcode(Opcode.INVOKE_VIRTUAL) // Add View (Story)
    )
)
