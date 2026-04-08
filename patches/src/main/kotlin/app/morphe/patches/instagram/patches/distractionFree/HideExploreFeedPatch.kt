package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.utility.replaceJsonFieldWithBogus
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM

private const val EXPLORE_KEY_TO_BE_HIDDEN = "sectional_items"

private object ExploreResponseJsonParserFingerprint : Fingerprint(
    strings = listOf(EXPLORE_KEY_TO_BE_HIDDEN, "clusters"),
    name = "unsafeParseFromJson"
)


@Suppress("unused")
val hideExploreFeedPatch = bytecodePatch(
    name = "Hide explore feed",
    description = "Hides posts and reels from the explore/search page."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        ExploreResponseJsonParserFingerprint.method.replaceJsonFieldWithBogus(EXPLORE_KEY_TO_BE_HIDDEN)
    }
}
