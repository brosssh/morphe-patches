package app.morphe.patches.instagram.misc.extension

import app.morphe.patches.instagram.misc.extension.hooks.applicationInitHook
import app.morphe.patches.shared.misc.extension.sharedExtensionPatch

val sharedExtensionPatch = sharedExtensionPatch(
    "instagram",
    false,
    applicationInitHook
)
