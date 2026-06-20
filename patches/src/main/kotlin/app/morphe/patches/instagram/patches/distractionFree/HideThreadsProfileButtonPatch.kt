package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.overrideMobileConfigBooleanFlag

@Suppress("unused")
val hideThreadsProfileButtonPatch = bytecodePatch(
    name = "Hide Threads profile button",
    description = "Hides the Threads button from the profile page action bar (top right of the profile page).",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(
        overrideMobileConfigBooleanFlag(
            override = "58467::20" to false // ig_channels_4th_ufi::is_ig_to_p92_app_switcher_enabled_android
        )
    )
}
