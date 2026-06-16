/*
 * Copyright (C) 2026 piko <https://github.com/crimera/piko>
 *
 * See the included NOTICE file for GPLv3 §7(b) terms that apply to this code.
 */

package app.morphe.patches.instagram.patches.misc

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.patches.extension.instagramExtensionPatch
import app.morphe.util.cloneMutable
import app.morphe.util.cloneMutableAndPreserveParameters
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/flags/OverrideMobileConfigBooleanFlagPatch;"

private object GetOverridesFingerprint : Fingerprint (
    definingClass = EXTENSION_CLASS_DESCRIPTOR,
    name = "<clinit>"
)

private object GetBoolValueForFlagFingerprint : Fingerprint (
    classFingerprint = Fingerprint(
        strings = listOf("__fbt_null__"),
        returnType = "Ljava/lang/String;"
    ),
    parameters = listOf("L", "J", "Z"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL)
)

private object GetUniversalIdFingerprint : Fingerprint (
    parameters = listOf("J"),
    returnType = "I",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    filters = listOf(
        literal(0xffff),
        opcode(Opcode.NEW_ARRAY)
    )
)

private val overrideMobileConfigBooleanFlagPatch = bytecodePatch {
    dependsOn(instagramExtensionPatch)

    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        val getUniversalIdMethod = GetUniversalIdFingerprint.method

        GetBoolValueForFlagFingerprint.method.cloneMutableAndPreserveParameters().addInstructionsWithLabels(
            0,
            """
                invoke-static {p2, p3}, ${getUniversalIdMethod.definingClass}->${getUniversalIdMethod.name}(J)I
                move-result v0
                invoke-static {v0}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;
                move-result-object v0
                
                invoke-static {p2, p3}, $EXTENSION_CLASS_DESCRIPTOR->getMobileConfigFlagId(J)J
                move-result-wide v1
                invoke-static {v1, v2}, Ljava/lang/String;->valueOf(J)Ljava/lang/String;
                move-result-object v1
                
                invoke-static {p2, p3, v0, v1}, $EXTENSION_CLASS_DESCRIPTOR->overrideBooleanFlag(JLjava/lang/String;Ljava/lang/String;)Ljava/lang/Boolean;
                move-result-object v0

                # Check if the result is NULL
                if-eqz v0, :continue
                
                invoke-virtual {v0}, Ljava/lang/Boolean;->booleanValue()Z
                move-result v1
                return v1
                
                :continue
                nop 
                """
        )

        // Expands the override registers count to allow the override patch to write in it
        GetOverridesFingerprint.method.cloneMutable(additionalRegisters = 3)
    }
}

fun overrideMobileConfigBooleanFlag(
    name: String? = null,
    description: String? = null,
    default: Boolean = true,

    override: Pair<String, Boolean>
) = bytecodePatch(
    name = name,
    description = description,
    default = default
) {
    dependsOn(overrideMobileConfigBooleanFlagPatch)

    compatibleWith(COMPATIBILITY_INSTAGRAM)

    execute {
        GetOverridesFingerprint.method.apply {
            val returnIndex = indexOfFirstInstructionOrThrow(Opcode.RETURN_VOID)

            addInstructions(
                returnIndex,
                """
                const-string v0, "${override.first}"
                const/4 v1, ${if (override.second) 0x1 else 0x0}
                
                invoke-static {v1}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v1
                
                sget-object v2, $EXTENSION_CLASS_DESCRIPTOR->OVERRIDES:Ljava/util/Map;
                invoke-interface {v2, v0, v1}, Ljava/util/Map;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            """
            )
        }
    }
}
