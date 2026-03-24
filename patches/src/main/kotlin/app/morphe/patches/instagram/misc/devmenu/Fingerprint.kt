package app.morphe.patches.instagram.misc.devmenu

import app.morphe.patcher.Fingerprint

internal val clearNotificationReceiverFingerprint = Fingerprint(
    name = "onReceive",
    definingClass = "Lcom/instagram/notifications/push/ClearNotificationReceiver;",
    strings = listOf ("NOTIFICATION_DISMISSED")
)
