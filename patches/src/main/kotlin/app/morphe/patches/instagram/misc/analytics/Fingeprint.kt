package app.morphe.patches.instagram.misc.analytics

import app.morphe.patcher.Fingerprint

internal object InstagramAnalyticsUrlBuilderMethodFingerprint : Fingerprint (
    strings = listOf("/logging_client_events")
)

internal const val TARGET_URL = "https://graph.facebook.com/logging_client_events"

internal object FacebookAnalyticsUrlInitMethodFingerprint : Fingerprint(
    strings = listOf("analytics_endpoint", TARGET_URL)
)
