package app.morphe.patches.mapy.misc.spoof

import app.morphe.patches.shared.Constants
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val bypassSignatureCheck = bytecodePatch(
    name = "Bypass signature check",
) {
    compatibleWith(Constants.COMPATIBILITY_MAPY)

    execute {
        securityCheckFingerprint.method.addInstruction(0, "return-void")

    }
}
