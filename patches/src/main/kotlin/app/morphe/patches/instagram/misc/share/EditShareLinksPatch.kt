package app.morphe.patches.instagram.misc.share

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

context(BytecodePatchContext)
internal fun editShareLinksPatch(block: MutableMethod.(index: Int, register: Int) -> Unit) {
    val fingerprintsToPatch = arrayOf(
        permalinkResponseJsonParserFingerprint,
        storyUrlResponseJsonParserFingerprint,
        profileUrlResponseJsonParserFingerprint
    )

    fingerprintsToPatch.forEachIndexed { index, fingerprint ->

        fingerprint.method.apply {
            val putSharingUrlIndex = indexOfFirstInstruction(
                index,
                Opcode.IPUT_OBJECT
            )
            val sharingUrlRegister = getInstruction<TwoRegisterInstruction>(putSharingUrlIndex).registerA
            block(putSharingUrlIndex, sharingUrlRegister)
        }
    }
}
