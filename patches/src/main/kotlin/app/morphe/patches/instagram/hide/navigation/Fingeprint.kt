package app.morphe.patches.instagram.hide.navigation

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object InitializeNavigationButtonsListFingerprint : Fingerprint (
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Lcom/instagram/common/session/UserSession;", "Z"),
    returnType = "Ljava/util/List;"
)

internal object NavigationButtonsEnumInitFingerprint : Fingerprint (
    name = "<init>",
    classFingerprint = Fingerprint(
        strings = listOf("FEED", "fragment_feed", "SEARCH", "fragment_search")
    )
)
