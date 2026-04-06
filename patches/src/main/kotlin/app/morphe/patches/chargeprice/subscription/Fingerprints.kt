package app.morphe.patches.chargeprice.subscription

import app.morphe.patcher.Fingerprint

internal const val userSettingResponseMetaClassName =
    "Lfr/chargeprice/core/internal/remote/user/response/UserSettingsResponse${"$"}Meta;"

internal object UserSettingParserFingerprint : Fingerprint (
    name = "parseUserSettings",
    definingClass = "Lfr/chargeprice/core/publics/controller/user/UserDataControllerProcessingKt;",
    parameters = listOf("L", "L", userSettingResponseMetaClassName)
)
