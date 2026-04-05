package app.morphe.patches.instagram.reels

import app.morphe.patcher.Fingerprint

internal object ClipsViewPagerImplGetViewAtIndexFingerprint : Fingerprint(
    strings = listOf("ClipsViewPagerImpl_getViewAtIndex")
)

internal object ClipsSwipeRefreshLayoutOnInterceptTouchEventFingerprint : Fingerprint (
    parameters = listOf("Landroid/view/MotionEvent;"),
    definingClass = "Linstagram/features/clips/viewer/ui/ClipsSwipeRefreshLayout;"
)
