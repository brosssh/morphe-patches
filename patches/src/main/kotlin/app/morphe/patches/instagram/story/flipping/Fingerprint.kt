package app.morphe.patches.instagram.story.flipping

import app.morphe.patcher.Fingerprint

internal val onStoryTimeoutActionFingerprint = Fingerprint(
    parameters = listOf("Ljava/lang/Object;"),
    returnType = "V",
    strings = listOf("userSession"),
    definingClass = "Linstagram/features/stories/fragment/ReelViewerFragment;"
)
