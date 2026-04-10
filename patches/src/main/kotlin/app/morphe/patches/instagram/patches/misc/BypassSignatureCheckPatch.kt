package app.morphe.patches.instagram.patches.misc

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private object IsValidSignatureMethodFingerprint : Fingerprint (
    Fingerprint(
        strings = listOf("The provider for uri '", "' is not trusted: ")
    ),
    parameters = listOf("L", "Z"),
    returnType = "Z",
    custom = { method, _ ->
        method.indexOfFirstInstruction {
            getReference<MethodReference>()?.name == "keySet"
        } >= 0
    }
)


@Suppress("unused")
val bypassSignatureCheckPatch = bytecodePatch(
    name = "Bypass signature check",
    default = true
) {
    compatibleWith(Constants.COMPATIBILITY_INSTAGRAM)

    execute {
        IsValidSignatureMethodFingerprint.method.returnEarly(true)
    }

}

