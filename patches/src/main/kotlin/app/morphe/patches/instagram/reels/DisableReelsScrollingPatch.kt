package app.morphe.patches.instagram.reels

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly

@Suppress("unused")
val disableReelsScrollingPatch = bytecodePatch(
    name = "Disable Reels scrolling",
    description = "Disables the endless scrolling behavior in Instagram Reels, preventing swiping to the next Reel. " +
            "Note: On a clean install, the 'Tip' animation may appear but will stop on its own after a few seconds.",
    use = false
) {
    compatibleWith("com.instagram.android"("422.0.0.0.35"))

    execute {
        val viewPagerField = clipsViewPagerImplGetViewAtIndexFingerprint.classDef.fields.first {
            it.type == "Landroidx/viewpager2/widget/ViewPager2;"
        }

        // Disable user input on the ViewPager2 to prevent scrolling.
        clipsViewPagerImplGetViewAtIndexFingerprint.method.addInstructions(
            0,
            """
               iget-object v0, p0, $viewPagerField
               const/4 v1, 0x0
               invoke-virtual { v0, v1 }, Landroidx/viewpager2/widget/ViewPager2;->setUserInputEnabled(Z)V
            """
        )

        // Return false in onInterceptTouchEvent to disable pull-to-refresh.
        clipsSwipeRefreshLayoutOnInterceptTouchEventFingerprint.method.returnEarly(false)
    }
}
