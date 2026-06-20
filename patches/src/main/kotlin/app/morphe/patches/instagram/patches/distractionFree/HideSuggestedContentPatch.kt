package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.overrideMobileConfigBooleanFlag

@Suppress("unused")
val hideSuggestedContentPatch = bytecodePatch(
    name = "Hide suggested content",
    description = "Choose the suggested content to hide using the patch options.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    val hideSuggestedReels by booleanOption(
        key = "hideSuggestedReels",
        default = true,
        title = "Hide suggested reels",
        description = "Hides suggested reels from feed and reels tab."
    )

    val hideSuggestedStories by booleanOption(
        key = "hideSuggestedStories",
        default = true,
        title = "Hide suggested stories",
        description = "Hides suggested stories/users from story tray."
    )

    val hideSuggestedSearches by booleanOption(
        key = "hideSuggestedSearches",
        default = true,
        title = "Hide suggested searches",
        description = "Hides suggested users/tag from the search bar."
    )

    val testKey by booleanOption(
        key = "testKey",
        default = true,
        title = "TESTTTT",
        description = "Hides suggested users/tag from the search bar."
    )

    if(testKey == true) {
        dependsOn(hideThreadsProfileButtonPatch)
    }

    if (hideSuggestedReels == true) {
        dependsOn(hideSuggestedReelsPatch)
    }

    if (hideSuggestedStories == true)
        dependsOn(hideSuggestedStoriesPatch)

    if (hideSuggestedSearches == true)
        dependsOn(
            overrideMobileConfigBooleanFlag(
                override = "111509::3" to false // ig_search_ta_nullstate_suggestions::is_android_enabled
            )
        )
}
