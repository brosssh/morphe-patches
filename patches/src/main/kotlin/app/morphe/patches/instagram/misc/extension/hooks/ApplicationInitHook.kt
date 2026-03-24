package app.morphe.patches.instagram.misc.extension.hooks

import app.morphe.patcher.Fingerprint
import app.morphe.patches.shared.misc.extension.ExtensionHook

internal val applicationInitHook = ExtensionHook(
    fingerprint = Fingerprint(
        custom = { method, classDef ->
            method.name == "onCreate" && classDef.endsWith("/InstagramAppShell;")
        }
    )
)
