package app.morphe.patches.mapy.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock premium features",
) {
    compatibleWith("cz.seznam.mapy"("9.70.1"))

    execute {
        userInfoFromJsonFingerprint.method.apply {
            val stringAssignInstructionIndex = userInfoFromJsonFingerprint.stringMatches!!.first().index

            val resultPremiumIndex = indexOfFirstInstructionOrThrow(
                stringAssignInstructionIndex,
                Opcode.MOVE_RESULT,
            )

            val premiumRegister = getInstruction<OneRegisterInstruction>(resultPremiumIndex).registerA

            addInstruction(resultPremiumIndex + 1,
                "const/16 v${premiumRegister}, 0x1")
        }

        featuresSyntheticInitFingerprint.method.addInstructions(0,
            """  
               const/4 p2, 0x1      # userBadge
               const/4 p3, 0x1      # advancedMyMaps
               const/4 p4, 0x1      # premiumSupport
               const/4 p5, -0x1     # offlineMapCount
               const/4 p6, 0x1      # customNavigationSpeeds
               const/4 p7, 0x1      # advancedRouting
               const/4 p8, 0x1      # watchSupport
               const/4 p9, 0x1      # peakfinder
            """
        )
    }
}
