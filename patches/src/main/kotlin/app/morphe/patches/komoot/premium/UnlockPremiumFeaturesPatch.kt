package app.morphe.patches.komoot.premium

import app.morphe.patches.shared.Constants
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.indexOfFirstInstructionReversed
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val unlockProPatch = bytecodePatch(
    name = "Unlock Premium features",
    description = "Unlock Premium features. Some features are not possible to patch (server sided), " +
            "such as saving a multi-days hike."
) {
    compatibleWith(Constants.COMPATIBILITY_KOMOOT)

    execute {
        premiumConfigFingerprint.method.apply {
            addInstruction(
                0,
                "sget-object p22, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;"
            )
        }

        publicUserInitFingerprint.method.addInstruction(
                0,
                "const/4 p5, 0x1"
        )

        fun MutableMethod.grantRoutingPermission() {
            val index = indexOfFirstInstructionReversed(Opcode.IPUT_OBJECT)

            val routingStatusRegister = getInstruction<TwoRegisterInstruction>(index).registerA

            addInstructionsAtControlFlowLabel(
                index,
                "sget-object v$routingStatusRegister, Lde/komoot/android/services/api/model/RoutingPermission\$StatusPermission;->GRANTED:Lde/komoot/android/services/api/model/RoutingPermission\$StatusPermission;",
            )
        }

        routingPermissionInitJsonFingerprint.method.grantRoutingPermission()

        routingPermissionInitFingerprint.method.grantRoutingPermission()
    }
}
