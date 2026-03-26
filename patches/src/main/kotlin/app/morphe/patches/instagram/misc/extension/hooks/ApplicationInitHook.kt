package app.morphe.patches.instagram.misc.extension.hooks

import app.morphe.patcher.Fingerprint
import app.morphe.patches.shared.misc.extension.ExtensionHook

internal val applicationInitHook = ExtensionHook(
    Fingerprint(
        name = "onCreate",
        definingClass = "/InstagramAppShell;"
    )
)
