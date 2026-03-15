package app.morphe.patches.mapy.misc.spoof

import app.morphe.patcher.fingerprint

internal val securityCheckFingerprint = fingerprint {
    custom { method, _ ->  method.name == "runSecurityChecks"}
}
