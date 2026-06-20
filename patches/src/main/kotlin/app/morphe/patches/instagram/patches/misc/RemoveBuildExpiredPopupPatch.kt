package app.morphe.patches.instagram.patches.misc

import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.removeBuildExpiredPopupPatch

@Suppress("unused")
val removeBuildExpiredPopupPatch = removeBuildExpiredPopupPatch(
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}
