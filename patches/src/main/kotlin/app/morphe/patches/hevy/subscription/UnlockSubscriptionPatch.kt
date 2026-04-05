package app.morphe.patches.hevy.subscription

import app.morphe.patches.shared.Constants
import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.shared.misc.hex.Replacement
import app.morphe.patches.shared.misc.hex.hexPatch

@Suppress("unused")
val unlockSubscriptionPatch = rawResourcePatch(
    name = "Unlock Pro features",
    description = "Unlocks Pro features. Most features in this app are server sided, so they can't be patched. " +
            "This should unlock everything which is possible to patch.",
) {
    compatibleWith(Constants.COMPATIBILITY_HEVY)

    dependsOn (
        hexPatch {
            setOf(
                Replacement(
                    "f8 ff 77 01 7a",
                    "f8 7f 77 01 7a",
                    "assets/index.android.bundle"

                ),
                Replacement(
                    "00 f8 ff 5c 01",
                    "00 f8 7f 5c 01",
                    "assets/index.android.bundle"
                ),
                Replacement(
                    "f8 ff 0d 03 00",
                    "f8 7f 0d 03 00",
                    "assets/index.android.bundle"
                ),
                Replacement(
                    "f8 ff 0d 03 00",
                    "f8 7f 0d 03 00",
                    "assets/index.android.bundle"
                ),
                Replacement(
                    "f8 ff 0d 03 00",
                    "f8 7f 0d 03 00",
                    "assets/index.android.bundle"
                ),
                Replacement(
                    "f9 90 07 00 79 00 5c",
                    "f9 90 07 00 78 00 5c",
                    "assets/index.android.bundle"
                )
            )
        }
    )
}
