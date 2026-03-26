package app.morphe.patches.instagram.misc.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.shared.replaceJsonFieldWithBogus

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables analytics that are sent periodically.",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        // Returns BOGUS as analytics url.
        instagramAnalyticsUrlBuilderMethodFingerprint.method.addInstructions(
        	0,
            """
                const-string v0, "BOGUS"
                return-object v0
            """
        )

        // Replaces analytics url as BOGUS.
        facebookAnalyticsUrlInitMethodFingerprint.method.replaceJsonFieldWithBogus(TARGET_URL)
    }
}
