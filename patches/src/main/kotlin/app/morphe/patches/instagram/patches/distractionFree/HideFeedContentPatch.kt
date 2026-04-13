package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.utility.replaceJsonFieldWithBogus

private const val FEED_ITEMS_KEY = "feed_items"

private object FeedContentJsonParserFingerprint : Fingerprint(
    strings = listOf(FEED_ITEMS_KEY, "auto_load_more_enabled"),
    name = "unsafeParseFromJson"
)

@Suppress("unused")
val hideFeedContent = bytecodePatch(
    name = "Hide feed content",
    description = "Hide the feed content, leaving only stories in the home page.",
    default = false
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        FeedContentJsonParserFingerprint.method.replaceJsonFieldWithBogus(FEED_ITEMS_KEY)
    }
}
