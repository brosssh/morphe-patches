package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.findInstructionIndicesReversedOrThrow
import app.morphe.util.fiveRegisters
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter

private object LiveTreeGetOptionalBooleanFingerprint : Fingerprint (
    name = "getOptionalBooleanValueNative",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL, AccessFlags.NATIVE),
    parameters = listOf("I"),
    returnType = "Ljava/lang/Boolean;"
)

@Suppress("unused")
val hideReshareButtonPatch = bytecodePatch(
    name = "Hide reshare button",
    description = "Hides the reshare button from both posts and reels.",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val proxyMethodName = "patch_getOptionalBoolean"
        val hashedFieldInteger = "enable_media_notes_production".hashCode() // -545107410

        LiveTreeGetOptionalBooleanFingerprint.classDef.apply {
            val nativeMethodName = LiveTreeGetOptionalBooleanFingerprint.method.name
            val classType = type

            methods.add(
                ImmutableMethod(
                    classType,
                    proxyMethodName,
                    listOf(ImmutableMethodParameter("I", null, null)),
                    "Ljava/lang/Boolean;",
                    AccessFlags.PUBLIC.value or AccessFlags.FINAL.value,
                    null,
                    null,
                    MutableMethodImplementation(3),
                ).toMutable().apply {
                    addInstructions(
                        0,
                        """
                            const v0, $hashedFieldInteger
                            if-ne p1, v0, :call_native
                            sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                            return-object v0
                            :call_native
                            invoke-virtual {p0, p1}, $classType->$nativeMethodName(I)Ljava/lang/Boolean;
                            move-result-object v0
                            return-object v0
                        """
                    )
                }
            )
        }

        val nativeCall = methodCall(
            definingClass = LiveTreeGetOptionalBooleanFingerprint.method.definingClass,
            name = LiveTreeGetOptionalBooleanFingerprint.method.name
        )

        Fingerprint(
            filters = listOf(nativeCall),
            custom = { method, _ ->
                method.name != proxyMethodName
            }
        ).matchAll().forEach { match ->
            match.method.apply {
                findInstructionIndicesReversedOrThrow(nativeCall).forEach { index ->
                    val registers = fiveRegisters(index)

                    replaceInstruction(
                        index,
                        "invoke-virtual { $registers }, ${LiveTreeGetOptionalBooleanFingerprint.method.definingClass}->patch_getOptionalBoolean(I)Ljava/lang/Boolean;"
                    )
                }
            }
        }
    }
}
