package app.morphe.patches.instagram.patches.distractionFree.suggestedContent

import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val hideSuggestedContent = bytecodePatch(
    name = "Hide suggested content",
    default = true
) {
    dependsOn(hideSuggestedFeedItems, hideSuggestedStoriesPatch)
}
