package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.overrideMobileConfigBooleanFlag

@Suppress("unused")
val hideInstantsPatch = bytecodePatch(
    name = "Hide Instants",
    description = "Hides Instants from DMs page.",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(
        overrideMobileConfigBooleanFlag(
            override = "71567::9" to false // ig_studio_quick_snap_android::direct_variant_enabled
        ),
        overrideMobileConfigBooleanFlag(
            override = "71567::42" to false // ig_studio_quick_snap_android::direct_variant_enabled_igid
        )
    )
}
