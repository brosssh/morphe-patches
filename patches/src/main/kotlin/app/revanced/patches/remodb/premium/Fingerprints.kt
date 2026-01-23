package app.revanced.patches.remodb.premium

import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val userDtoConstructorFingerprint = fingerprint {
    custom { methodDef, classDef ->
        classDef.equals("Lde/c;") &&
                methodDef.name == "<init>"
    }
}
