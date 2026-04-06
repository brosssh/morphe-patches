package app.morphe.patches.instagram.feed

import app.morphe.patcher.Fingerprint

internal object MainFeedRequestClassFingerprint : Fingerprint (
    strings = listOf("Request{mReason=", ", mInstanceNumber=")
)

internal object InitMainFeedRequestFingerprint : Fingerprint(
    name =  "<init>",
    classFingerprint = MainFeedRequestClassFingerprint
)

internal object MainFeedHeaderMapFinderFingerprint : Fingerprint (
    strings = listOf("pagination_source", "FEED_REQUEST_SENT")
)
