package app.morphe.patches.instagram.misc.analytics

import app.morphe.patcher.Fingerprint

internal val instagramAnalyticsUrlBuilderMethodFingerprint = Fingerprint (
    strings = listOf("/logging_client_events")
)

internal const val TARGET_URL = "https://graph.facebook.com/logging_client_events"

internal val facebookAnalyticsUrlInitMethodFingerprint = Fingerprint(
    strings = listOf("analytics_endpoint", TARGET_URL)
)
