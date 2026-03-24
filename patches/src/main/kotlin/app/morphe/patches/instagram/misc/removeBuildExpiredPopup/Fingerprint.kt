package app.morphe.patches.instagram.misc.removeBuildExpiredPopup

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.literal

internal const val MILLISECOND_IN_A_DAY_LITERAL = 0x5265c00L

internal val appUpdateLockoutBuilderFingerprint = Fingerprint(
    strings = listOf("android.hardware.sensor.hinge_angle"),
    filters = listOf(literal(MILLISECOND_IN_A_DAY_LITERAL))
)
