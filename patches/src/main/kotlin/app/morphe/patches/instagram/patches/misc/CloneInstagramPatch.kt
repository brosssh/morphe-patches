package app.morphe.patches.instagram.patches.misc

import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.clonePatch

@Suppress("unused")
val clonePatch = clonePatch(
    defaultPackageName = "com.instagram.android.brosssh",
    defaultAppName = "Instagram Distraction Free",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}
