package app.morphe.patches.instagram.feed

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.BytecodePatchContext

internal val mainFeedRequestClassFingerprint = Fingerprint (
    strings = listOf("Request{mReason=", ", mInstanceNumber=")
)

context(BytecodePatchContext)
internal val initMainFeedRequestFingerprint get() = Fingerprint(
    name =  "<init>",
    definingClass = mainFeedRequestClassFingerprint.classDef.type
)

internal val mainFeedHeaderMapFinderFingerprint = Fingerprint (
    strings = listOf("pagination_source", "FEED_REQUEST_SENT")
)
