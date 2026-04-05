package app.morphe.patches.instagram.ads

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

internal object AdInjectorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE),
    returnType = "Z",
    parameters = listOf("L", "L"),
    strings = listOf("SponsoredContentController.insertItem")
)

internal const val REEL_ADS_KEY = "ad_media_items"

internal object ReelAdsFingerprint : Fingerprint(
    filters = listOf(string(REEL_ADS_KEY)),
    name = "unsafeParseFromJson"
)
