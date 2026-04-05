package app.morphe.patches.instagram.hide.stories

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object GetOrCreateAvatarViewFingerprint : Fingerprint(
    parameters = emptyList(),
    returnType = "L",
    definingClass = "Lcom/instagram/reels/ui/views/reelavatar/RecyclerReelAvatarView;",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_VIRTUAL,
        Opcode.IPUT_OBJECT,
        Opcode.INVOKE_VIRTUAL // Add View (Story)
    )
)
