package app.morphe.patches.instagram.ads

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val adInjectorFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE),
    returnType = "Z",
    parameters = listOf("L", "L"),
    strings = listOf("SponsoredContentController.insertItem")
)
