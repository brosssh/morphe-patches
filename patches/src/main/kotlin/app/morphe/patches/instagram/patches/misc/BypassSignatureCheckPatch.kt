package app.morphe.patches.instagram.patches.misc

import app.morphe.patches.Constants.COMPATIBILITY_INSTAGRAM
import app.morphe.patches.instagram.misc.bypassSignatureCheckPatch

@Suppress("unused")
val bypassSignatureCheckPatch = bypassSignatureCheckPatch(
    default = true
) {
    compatibleWith(COMPATIBILITY_INSTAGRAM)
}

