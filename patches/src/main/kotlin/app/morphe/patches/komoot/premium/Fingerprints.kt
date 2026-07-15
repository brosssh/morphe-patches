package app.morphe.patches.komoot.premium

import app.morphe.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val premiumConfigFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
    parameters(
        "L", "L", "L", "L", "L", "L", "L", "L", "L", "L", "L",
        "L", "L", "L", "L", "L", "L", "L", "L", "L", "L", "L",
        "L"
    )
    custom { _, classDef ->
        classDef.endsWith("api/model/AppConfigV3;")
    }
}

internal val publicUserInitFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
    parameters("L", "L", "L", "L", "Z")
    custom { _, classDef ->
        classDef.endsWith("api/model/PublicUserProfileV7;")
    }
}

private const val routingPermissionClassName = "Lde/komoot/android/services/api/model/RoutingPermission"

internal val routingPermissionInitFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR)
    parameters("$routingPermissionClassName${"$"}StatusPermission;")
    custom { _, classDef ->
        classDef.type == "$routingPermissionClassName;"
    }
}
