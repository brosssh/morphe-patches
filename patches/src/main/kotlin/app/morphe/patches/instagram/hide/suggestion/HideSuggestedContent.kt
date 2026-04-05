package app.morphe.patches.instagram.hide.suggestion

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.shared.replaceJsonFieldWithBogus

@Suppress("unused")
val hideSuggestedContent = bytecodePatch(
    name = "Hide suggested content",
    description = "Hides suggested stories, reels, threads and survey from feed (Suggested posts will still be shown).",
    default = true,
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        FEED_ITEM_KEYS_TO_BE_HIDDEN.forEach { key ->
            FeedItemParseFromJsonFingerprint.method.replaceJsonFieldWithBogus(key)
        }
    }
}
