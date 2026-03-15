package app.morphe.patches.shared

import app.morphe.patcher.fingerprint

internal val castContextFetchFingerprint = fingerprint {
    strings("Error fetching CastContext.")
}

internal val primeMethodFingerprint = fingerprint {
    strings("com.google.android.GoogleCamera", "com.android.vending")
}
