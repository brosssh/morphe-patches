package app.morphe.patches.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_CALISTREE = Compatibility(
        name = "Calistree",
        packageName = "com.calistree.calistree",
        appIconColor = 0xFDAD16,
        targets = listOf(AppTarget(version = "4.17.8")),
    )

    val COMPATIBILITY_CHARGEPRICE = Compatibility(
        name = "Chargeprice",
        packageName = "fr.chargeprice.app",
        appIconColor = 0x007AFF,
        targets = listOf(AppTarget(version = "3.3.0")),
    )

    val COMPATIBILITY_GEOCACHING = Compatibility(
        name = "Geocaching",
        packageName = "com.groundspeak.geocaching.intro",
        appIconColor = 0X1AB268,
    )

    val COMPATIBILITY_HEVY = Compatibility(
        name = "Hevy",
        packageName = "com.hevy",
        appIconColor = 0x000000,
        targets = listOf(AppTarget(version = "2.0.0")),
    )

    val COMPATIBILITY_KOMOOT = Compatibility(
        name = "Komoot",
        packageName = "de.komoot.android",
        appIconColor = 0xECEBB4,
        targets = listOf(AppTarget(version = "2025.38.2")),
    )

    val COMPATIBILITY_MAPY = Compatibility(
        name = "Mapy.com",
        packageName = "cz.seznam.mapy",
        appIconColor = 0x32CF11,
    )

    val COMPATIBILITY_MAPY_9_70_1 = Compatibility(
        name = "Mapy.com",
        packageName = "cz.seznam.mapy",
        appIconColor = 0x32CF11,
        targets = listOf(AppTarget(version = "9.70.1")),
    )

    val COMPATIBILITY_PARK4NIGHT = Compatibility(
        name = "park4night",
        packageName = "fr.tramb.park4night",
        appIconColor = 0xFFFFFF,
        targets = listOf(AppTarget(version = "7.1.11")),
    )
}
