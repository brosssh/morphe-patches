package app.morphe.patches.komoot.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.indexOfFirstInstructionReversed
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

private val premiumConfigFingerprint = Fingerprint(
    definingClass = "api/model/AppConfigV3;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("L", "L", "L", "L", "L", "L", "L", "L", "L", "L", "L",
        "L", "L", "L", "L", "L", "L", "L", "L", "L", "L", "L", "L"
    )
)
private val publicUserInitFingerprint = Fingerprint (
    definingClass = "api/model/PublicUserProfileV7;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("L", "L", "L", "L", "Z")
)

private const val routingPermissionClassName = "Lde/komoot/android/services/api/model/RoutingPermission"

private val routingPermissionInitFingerprint = Fingerprint (
    definingClass = "$routingPermissionClassName;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("$routingPermissionClassName${"$"}StatusPermission;")
)


@Suppress("unused")
val unlockProPatch = bytecodePatch(
    name = "Unlock Premium features",
    description = "Unlock Premium features. Some features are not possible to patch (server sided), " +
            "such as saving a multi-days hike."
) {
    compatibleWith(Constants.COMPATIBILITY_KOMOOT)

    execute {
        premiumConfigFingerprint.method.addInstruction(
            0,
            "sget-object p1, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;"
        )

        publicUserInitFingerprint.method.addInstruction(
            0,
            "const/4 p5, 0x1"
        )

        routingPermissionInitFingerprint.method.apply {
            val index = indexOfFirstInstructionReversed(Opcode.IPUT_OBJECT)

            val routingStatusRegister = getInstruction<TwoRegisterInstruction>(index).registerA

            addInstructionsAtControlFlowLabel(
                index,
                "sget-object v$routingStatusRegister, Lde/komoot/android/services/api/model/RoutingPermission\$StatusPermission;->GRANTED:Lde/komoot/android/services/api/model/RoutingPermission\$StatusPermission;",
            )
        }
    }
}
