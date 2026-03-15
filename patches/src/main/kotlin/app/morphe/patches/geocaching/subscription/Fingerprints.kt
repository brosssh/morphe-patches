package app.morphe.patches.geocaching.subscription

import app.morphe.patcher.fingerprint

internal val userProfileDeserializerFingerprint = fingerprint {
    custom { methodDef, classDef ->
        classDef.endsWith("UserProfileResponse\$\$serializer;") && methodDef.name == "deserialize"
    }
}

internal val ownProfileDeserializerFingerprint = fingerprint {
    custom { methodDef, classDef ->
        classDef.endsWith("OwnProfileResponse\$Profile\$\$serializer;") && methodDef.name == "deserialize"
    }
}
