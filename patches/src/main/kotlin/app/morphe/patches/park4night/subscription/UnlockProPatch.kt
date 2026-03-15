package app.morphe.patches.park4night.subscription

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val unlockProPatch = bytecodePatch(
    name = "Unlock Pro features",
    description = "Unlock Pro features (park4night +). You have to be logged in with an account."
) {
    compatibleWith("fr.tramb.park4night"("7.1.11"))

    execute {
        userDtoConstructorFingerprint.method.addInstruction(0, "const-string p21, \"2050-01-01\"")
    }
}
