package app.morphe.patches.instagram.hide.explore

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.shared.replaceJsonFieldWithBogus

@Suppress("unused")
val hideExploreFeedPatch = bytecodePatch(
    name = "Hide explore feed",
    description = "Hides posts and reels from the explore/search page.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        ExploreResponseJsonParserFingerprint.method.replaceJsonFieldWithBogus(EXPLORE_KEY_TO_BE_HIDDEN)
    }
}
