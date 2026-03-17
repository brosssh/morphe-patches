package app.morphe.patches.instagram.hide.navigation

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.AccessFlags

internal val initializeNavigationButtonsListFingerprint = Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Lcom/instagram/common/session/UserSession;", "Z"),
    returnType = "Ljava/util/List;"
)

private val navigationButtonsEnumClassDef = Fingerprint(
    strings = listOf("FEED", "fragment_feed", "SEARCH", "fragment_search")
)

context(BytecodePatchContext)
internal val navigationButtonsEnumInitFingerprint get() = Fingerprint (
    custom = { method, classDef ->
        method.name == "<init>"
                && classDef == navigationButtonsEnumClassDef.classDef
    }
)
