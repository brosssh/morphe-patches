package app.morphe.patches.instagram.hide.suggestion

import app.morphe.patcher.Fingerprint

internal val FEED_ITEM_KEYS_TO_BE_HIDDEN = arrayOf(
    "clips_netego",
    "stories_netego",
    "in_feed_survey",
    "bloks_netego",
    "suggested_igd_channels",
    "suggested_top_accounts",
    "suggested_users",
)

internal val feedItemParseFromJsonFingerprint = Fingerprint(
    strings = listOf(*FEED_ITEM_KEYS_TO_BE_HIDDEN, "FeedItem")
)
