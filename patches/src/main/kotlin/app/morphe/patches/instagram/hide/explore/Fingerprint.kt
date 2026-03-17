package app.morphe.patches.instagram.hide.explore

import app.morphe.patcher.Fingerprint

internal const val EXPLORE_KEY_TO_BE_HIDDEN = "sectional_items"

internal val exploreResponseJsonParserFingerprint = Fingerprint(
    strings = listOf(EXPLORE_KEY_TO_BE_HIDDEN, "clusters"),
    custom = { method, _ -> method.name == "unsafeParseFromJson" }
)
