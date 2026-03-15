package app.morphe.patches.park4night.subscription

import app.morphe.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val userDtoConstructorFingerprint = fingerprint {
    custom { methodDef, classDef ->
        classDef.equals("Lcom/park4night/p4nsharedlayers/data/dto/UserDto;") &&
                methodDef.name == "<init>"
    }
    accessFlags(AccessFlags.PUBLIC, AccessFlags.SYNTHETIC, AccessFlags.CONSTRUCTOR)
}
