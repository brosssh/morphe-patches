package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.utility.replaceJsonFieldWithBogus

private val FEED_ITEM_KEYS_TO_BE_HIDDEN = arrayOf(
    "clips_netego",
    "stories_netego",
    "in_feed_survey",
    "bloks_netego",
    "suggested_igd_channels",
    "suggested_top_accounts",
    "suggested_users",
    "suggested_businesses",
    "suggested_hashtags",
    "suggested_producers",
    "suggested_producers_v2",
    "suggested_close_friends",
    "suggested_shops"
)

private object FeedItemParseFromJsonFingerprint : Fingerprint(
    strings = listOf(*FEED_ITEM_KEYS_TO_BE_HIDDEN, "FeedItem")
)

@Suppress("unused")
val hideSuggestedContent = bytecodePatch(
    name = "Hide suggested content",
    description = "Hides suggested stories, reels, threads and survey from feed (Suggested posts will still be shown)."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val matchedMethod = FeedItemParseFromJsonFingerprint.method
        FEED_ITEM_KEYS_TO_BE_HIDDEN.forEach { key ->
            matchedMethod.replaceJsonFieldWithBogus(key)
        }
    }
}
