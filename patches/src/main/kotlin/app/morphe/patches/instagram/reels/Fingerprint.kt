package app.morphe.patches.instagram.reels

import app.morphe.patcher.Fingerprint

internal val clipsViewPagerImplGetViewAtIndexFingerprint = Fingerprint(
    strings = listOf("ClipsViewPagerImpl_getViewAtIndex")
)

internal val clipsSwipeRefreshLayoutOnInterceptTouchEventFingerprint  = Fingerprint (
    parameters = listOf("Landroid/view/MotionEvent;"),
    custom = { _, classDef -> classDef.type == "Linstagram/features/clips/viewer/ui/ClipsSwipeRefreshLayout;" }
)
