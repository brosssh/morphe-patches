package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.returnEarly

private object ClipsViewPagerImplGetViewAtIndexFingerprint : Fingerprint(
    strings = listOf("ClipsViewPagerImpl_getViewAtIndex")
)

private object ClipsSwipeRefreshLayoutOnInterceptTouchEventFingerprint : Fingerprint (
    parameters = listOf("Landroid/view/MotionEvent;"),
    definingClass = "Linstagram/features/clips/viewer/ui/ClipsSwipeRefreshLayout;"
)

@Suppress("unused")
val disableReelsScrollingPatch = bytecodePatch(
    name = "Disable Reels scrolling",
    description = "Disables the endless scrolling behavior in Instagram Reels, preventing swiping to the next Reel. " +
            "Note: On a clean install, the 'Tip' animation may appear but will stop on its own after a few seconds."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val viewPagerField = ClipsViewPagerImplGetViewAtIndexFingerprint.classDef.fields.first {
            it.type == "Landroidx/viewpager2/widget/ViewPager2;"
        }

        // Disable user input on the ViewPager2 to prevent scrolling.
        ClipsViewPagerImplGetViewAtIndexFingerprint.method.addInstructions(
            0,
            """
               iget-object v0, p0, $viewPagerField
               const/4 v1, 0x0
               invoke-virtual { v0, v1 }, Landroidx/viewpager2/widget/ViewPager2;->setUserInputEnabled(Z)V
            """
        )

        // Return false in onInterceptTouchEvent to disable pull-to-refresh.
        ClipsSwipeRefreshLayoutOnInterceptTouchEventFingerprint.method.returnEarly(false)
    }
}
