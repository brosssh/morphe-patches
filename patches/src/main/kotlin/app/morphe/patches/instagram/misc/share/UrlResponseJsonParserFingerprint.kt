package app.morphe.patches.instagram.misc.share

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object PermalinkResponseJsonParserFingerprint : Fingerprint (
    strings = listOf("permalink"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.NEW_INSTANCE,
        Opcode.INVOKE_DIRECT,
        Opcode.INVOKE_VIRTUAL
    ),
    name = "unsafeParseFromJson"
)

internal object StoryUrlResponseJsonParserFingerprint : Fingerprint (
    strings = listOf("story_item_to_share_url"),
    name = "unsafeParseFromJson"
)

internal object ProfileUrlResponseJsonParserFingerprint : Fingerprint (
    strings = listOf("profile_to_share_url"),
    name = "unsafeParseFromJson"
)
