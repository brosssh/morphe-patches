package app.morphe.patches.instagram.misc.devmenu

import app.morphe.patcher.Fingerprint

internal val clearNotificationReceiverFingerprint = Fingerprint(
    custom = { method, classDef ->
        method.name == "onReceive" &&
                classDef.type == "Lcom/instagram/notifications/push/ClearNotificationReceiver;"
    },
    strings = listOf ("NOTIFICATION_DISMISSED")
)
