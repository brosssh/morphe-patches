package app.morphe.patches.instagram

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

internal object Constants {
    val COMPATIBILITY_INSTAGRAM = Compatibility(
        name = "Instagram",
        packageName = "com.instagram.android",
        apkFileType = ApkFileType.APK,
        targets = listOf(AppTarget(version = "422.0.0.0.35"))
    )
}
