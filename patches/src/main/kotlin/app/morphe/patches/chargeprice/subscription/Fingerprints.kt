package app.morphe.patches.chargeprice.subscription

import app.morphe.patcher.fingerprint

internal const val userSettingResponseMetaClassName =
    "Lfr/chargeprice/core/internal/remote/user/response/UserSettingsResponse${"$"}Meta;"

internal val userSettingParserFingerprint = fingerprint {
    custom { methodDef, classDef ->
        classDef.equals("Lfr/chargeprice/core/publics/controller/user/UserDataControllerProcessingKt;") &&
                methodDef.name == "parseUserSettings"
    }
    parameters(
        "L",
        "L",
        userSettingResponseMetaClassName
    )
}
