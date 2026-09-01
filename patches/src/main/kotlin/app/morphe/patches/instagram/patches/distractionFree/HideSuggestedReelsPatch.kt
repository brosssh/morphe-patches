package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.library.instagram.utility.replaceJsonFieldWithBogus
import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.BytecodePatchContext

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
    "suggested_producers_v2",
    "suggested_producers",
    "suggested_close_friends",
    "suggested_shops"
)

private object FeedItemParseFromJsonFingerprint : Fingerprint(
    strings = listOf(*FEED_ITEM_KEYS_TO_BE_HIDDEN),
    name = "unsafeParseFromJson"
)

context(_: BytecodePatchContext)
fun hideSuggestedReelsPatch() = FeedItemParseFromJsonFingerprint.method.apply {
    FEED_ITEM_KEYS_TO_BE_HIDDEN.forEach { key ->
        replaceJsonFieldWithBogus(key)
    }
}
