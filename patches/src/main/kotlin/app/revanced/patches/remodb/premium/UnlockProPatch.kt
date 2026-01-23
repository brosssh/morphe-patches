package app.revanced.patches.remodb.premium

import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium features",
    description = "Unlock Premium features. You have to be logged in with an account."
) {
    compatibleWith("com.kriskast.remotedb"("5.2.4"))

    execute {
        userDtoConstructorFingerprint.method.addInstruction(0, "const/4 p2, 0x1")
    }
}
