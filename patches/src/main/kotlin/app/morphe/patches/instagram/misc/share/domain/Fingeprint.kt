package app.morphe.patches.instagram.misc.share.domain

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal const val EXTENSION_CLASS_DESCRIPTOR =
    "Lapp/morphe/extension/instagram/misc/share/domain/ChangeLinkSharingDomainPatch;"

internal object GetCustomShareDomainFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC),
    returnType = ("Ljava/lang/String;"),
    parameters = listOf(),
    name = "getCustomShareDomain",
    definingClass = EXTENSION_CLASS_DESCRIPTOR
)
