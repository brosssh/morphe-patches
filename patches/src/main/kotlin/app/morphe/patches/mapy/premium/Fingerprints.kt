package app.morphe.patches.mapy.premium

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object UserInfoFromJsonFingerprint : Fingerprint (
    name = "fromJson",
    definingClass = "/UserInfo\$Companion;",
    strings = listOf("premium")
)

internal object FeaturesSyntheticInitFingerprint : Fingerprint (
    name = "<init>",
    definingClass = "/FeaturesApiModel;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.SYNTHETIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf("I", "Z", "Z", "Z", "I", "Z", "Z", "Z", "Z", "Ljava/lang/Integer;", "L")
)
