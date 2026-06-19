package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.overrideMobileConfigBooleanFlag

@Suppress("unused")
val hideReelsSaveButtonPatch = bytecodePatch(
    name = "Hide Reels save button",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(
        overrideMobileConfigBooleanFlag(
            override = "81702::5" to true // ig_channels_4th_ufi::android_hide_save_button
        )
    )
}
