package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.instagram.patches.extension.instagramExtensionPatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/feed/LimitFeedToFollowedProfiles;"

private object MainFeedRequestClassFingerprint : Fingerprint (
    strings = listOf("Request{mReason=", ", mInstanceNumber=")
)

private object InitMainFeedRequestFingerprint : Fingerprint(
    name =  "<init>",
    classFingerprint = MainFeedRequestClassFingerprint
)

private object MainFeedHeaderMapFinderFingerprint : Fingerprint (
    strings = listOf("pagination_source", "FEED_REQUEST_SENT")
)

@Suppress("unused")
val limitFeedToFollowingProfiles = bytecodePatch(
    name = "Limit feed to following profiles",
    description = "Filters the home feed to display only content from profiles you follow."
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(instagramExtensionPatch)

    execute {
        /**
         * Since the header field is obfuscated and there is no easy way to identify it among all the class fields,
         * an additional method is fingerprinted.
         * This method uses the map, so we can get the field name of the map field using this.
         */
        val mainFeedRequestHeaderFieldName: String

        with(MainFeedHeaderMapFinderFingerprint.method) {
            mainFeedRequestHeaderFieldName = indexOfFirstInstructionOrThrow {
                getReference<FieldReference>().let { ref ->
                    ref?.type == "Ljava/util/Map;" &&
                            ref.definingClass == MainFeedRequestClassFingerprint.classDef.toString()

                }
            }.let { instructionIndex ->
                getInstruction(instructionIndex).getReference<FieldReference>()!!.name
            }
        }

        InitMainFeedRequestFingerprint.method.apply {
            // Finds the instruction where the map is being initialized in the constructor
            val getHeaderIndex = indexOfFirstInstructionOrThrow {
                getReference<FieldReference>().let {
                    it?.name == mainFeedRequestHeaderFieldName
                }
            }

            val paramHeaderRegister = getInstruction<TwoRegisterInstruction>(getHeaderIndex).registerA

            // Replace the `pagination_source` header value with `following` in the feed/timeline request.
            addInstructions(
                getHeaderIndex,
                """
                    invoke-static { v$paramHeaderRegister }, $EXTENSION_CLASS_DESCRIPTOR->setFollowingHeader(Ljava/util/Map;)Ljava/util/Map;
                    move-result-object v$paramHeaderRegister
                """
            )
        }
    }
}
