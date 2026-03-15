package app.morphe.patches.chargeprice.subscription

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium features",
    description = "Unlock Premium features. You have to be logged in with an account."
) {
    compatibleWith("fr.chargeprice.app"("3.3.0"))

    execute {
        userSettingParserFingerprint.method.addInstructions(
            0,
            """
                new-instance v0, Ljava/util/ArrayList;
                invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V
                const-string v1, "MOBILE_PREMIUM"
                invoke-virtual {v0, v1}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z
                invoke-virtual {p2, v0}, $userSettingResponseMetaClassName->copy(Ljava/util/List;)$userSettingResponseMetaClassName
                move-result-object p2
            """
        )
    }
}
