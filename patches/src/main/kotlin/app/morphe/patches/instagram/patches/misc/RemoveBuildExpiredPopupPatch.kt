package app.morphe.patches.instagram.patches.misc

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

object SnoozeExpirationManagerInitFingerprint : Fingerprint(
    Fingerprint(
        strings = listOf("disabled_snooze_expiration_lockout_manager"),
        returnType = "Z"
    ),
    name = "<init>",
    parameters = listOf("L", "L", "L", "L", "L", "L", "L" ,"L", "L", "I", "Z", "Z")
)

@Suppress("unused")
val removeBuildExpiredPopupPatch = bytecodePatch(
    name = "Remove build expired popup",
    description = "Removes the popup that appears after a while, when the app version ages."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        SnoozeExpirationManagerInitFingerprint.method.addInstruction(
            0,
            "const/4 p10, 0x1"
        )
    }
}
