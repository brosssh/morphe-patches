package app.morphe.patches

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.SupportedAbi

object Constants {
    val COMPATIBILITY_INSTAGRAM = Compatibility(
        name = "Instagram",
        packageName = "com.instagram.android",
        targets = listOf(
            AppTarget(
                version = "439.0.0.37.89",
                versionCodes = mapOf(
                    SupportedAbi.ARM64_V8A to 384510827,
                    SupportedAbi.ARMEABI_V7A to 384510838
                )
            )
        )
    )

    val COMPATIBILITY_CHARGEPRICE = Compatibility(
        name = "Chargeprice",
        packageName = "fr.chargeprice.app",
        appIconColor = 0x007AFF,
        targets = listOf(AppTarget(version = "3.9.2")),
    )

    val COMPATIBILITY_KOMOOT = Compatibility(
        name = "Komoot",
        packageName = "de.komoot.android",
        appIconColor = 0xECEBB4,
        targets = listOf(AppTarget(version = "2026.26.7")),
    )

    val COMPATIBILITY_MAPY = Compatibility(
        name = "Mapy.com",
        packageName = "cz.seznam.mapy",
        appIconColor = 0x32CF11,
        targets = listOf(AppTarget(version = "26.5.0")),
    )

    val COMPATIBILITY_PARK4NIGHT = Compatibility(
        name = "park4night",
        packageName = "fr.tramb.park4night",
        appIconColor = 0xFFFFFF,
        targets = listOf(AppTarget(version = "7.1.11")),
    )
}
