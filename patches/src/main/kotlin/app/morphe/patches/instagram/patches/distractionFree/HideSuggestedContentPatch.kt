package app.morphe.patches.instagram.patches.distractionFree

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.patches.misc.overrideMobileConfigBooleanFlag
import app.morphe.patches.instagram.utility.JsonParserFingerprint
import app.morphe.patches.instagram.utility.replaceJsonFieldWithBogus
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstructionOrThrow
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/hide/suggestedContent/HideSuggestedContentPatch;"

private val FEED_ITEM_KEYS_TO_BE_HIDDEN = arrayOf(
    "clips_netego",
    "stories_netego",
    "in_feed_survey",
    "bloks_netego",
    "suggested_igd_channels",
    "suggested_top_accounts",
    "suggested_users",
    "suggested_businesses",
    "suggested_hashtags",
    "suggested_producers",
    "suggested_producers_v2",
    "suggested_close_friends",
    "suggested_shops"
)

private object FeedItemParseFromJsonFingerprint : Fingerprint(
    strings = listOf(*FEED_ITEM_KEYS_TO_BE_HIDDEN, "FeedItem")
)

private object StoryItemParseFromJsonFingerprint : JsonParserFingerprint(
    "reel_type",
    "ReelResponseItem"
)

@Suppress("unused")
val hideSuggestedContent = bytecodePatch(
    name = "Hide suggested content",
    description = "Hides suggested stories, reels, threads and survey from feed (Suggested posts will still be shown).",
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)

    dependsOn(
        overrideMobileConfigBooleanFlag(
            override = "111509::3" to false // ig_search_ta_nullstate_suggestions::is_android_enabled
        )
    )

    execute {
        FEED_ITEM_KEYS_TO_BE_HIDDEN.forEach { key ->
            FeedItemParseFromJsonFingerprint.method.replaceJsonFieldWithBogus(key)
        }

        with(StoryItemParseFromJsonFingerprint.match()) {
            method.apply {
                val reelTypeResultIndex = indexOfFirstInstructionOrThrow(matchIndex) {
                    getReference<MethodReference>()?.returnType == "Ljava/lang/String;"
                } + 1

                val reelTypeResultRegister = getInstruction<OneRegisterInstruction>(reelTypeResultIndex).registerA

                addInstructionsWithLabels(
                    reelTypeResultIndex + 1,
                    """
                        invoke-static { v$reelTypeResultRegister }, $EXTENSION_CLASS_DESCRIPTOR->getValidStoryType(Ljava/lang/String;)Ljava/lang/String;
                        move-result-object v$reelTypeResultRegister
                        
                        # if getValidStoryType return null, make the method return null
                        if-nez v$reelTypeResultRegister, :continue
                        const/4 v$reelTypeResultRegister, 0x0
                        return-object v$reelTypeResultRegister
                        :continue
                        nop
                    """
                )
            }
        }
    }
}
