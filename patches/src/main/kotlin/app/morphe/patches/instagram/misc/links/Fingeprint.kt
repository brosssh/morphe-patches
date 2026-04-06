package app.morphe.patches.instagram.misc.links

import app.morphe.patcher.Fingerprint

internal const val TARGET_STRING = "Tracking.ARG_CLICK_SOURCE"

internal object InAppBrowserFunctionFingerprint : Fingerprint(
    returnType = ("Z"),
    strings = listOf("TrackingInfo.ARG_MODULE_NAME", TARGET_STRING)
)
