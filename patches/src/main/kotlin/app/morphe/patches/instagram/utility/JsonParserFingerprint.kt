package app.morphe.patches.instagram.utility

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.Match
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.string
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.getReference
import app.morphe.util.indexOfFirstInstruction
import app.morphe.util.indexOfFirstStringInstruction
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
import com.android.tools.smali.dexlib2.iface.instruction.SwitchPayload
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import java.util.logging.Logger

private const val STRING_DICTIONARY_CLASS = "LX/000;"
private const val STRING_DICTIONARY_METHOD = "A00"

data class JsonParserMatch(
    val method: MutableMethod,
    val matchIndex: Int,
    val isStringMatch: Boolean
)

internal open class JsonParserFingerprint(
    private val mainStringKey: String,
    private vararg val otherStringKeys: String
) {
    private val allStringKeys get() = listOf(mainStringKey) + otherStringKeys

    context(_: BytecodePatchContext)
    private val switchKeys get() = allStringKeys.associateWith { findSwitchKeyForString(it) }

    context(_: BytecodePatchContext)
    fun match(): JsonParserMatch = matchAll().let {
        if (it.size > 1) {
            Logger.getLogger(this::class.java.name)
                .warning("SingleMatch was called but multiple fingerprints where matched")
        }
        it.firstOrNull()
    } ?: throw PatchException("No match found for key: $mainStringKey")

    context(_: BytecodePatchContext)
    fun matchAll(): List<JsonParserMatch> =
        (normalMatches() + complexMatches())
            .distinctBy { it.method }

    context(_: BytecodePatchContext)
    private fun normalMatches(): List<JsonParserMatch> =
        normalFingerprint.matchAllOrNull()?.mapNotNull { result ->
            val allKeysPresent = otherStringKeys.all { key ->
                result.method.containsKey(key, switchKeys[key])
            }
            if (!allKeysPresent) return@mapNotNull null

            JsonParserMatch(
                method = result.method,
                matchIndex = normalFingerprint.stringMatches.first().index,
                isStringMatch = true
            )
        } ?: emptyList()

    context(_: BytecodePatchContext)
    private fun complexMatches(): List<JsonParserMatch> {
        // If the main key isn't in the dictionary at all, nothing to match
        val mainSwitchKey = switchKeys[mainStringKey] ?: return emptyList()

        return jsonParserFullList.mapNotNull { result ->
            val matchIndex = result.method.indexOfFirstDictionaryCall(mainSwitchKey)
                ?: return@mapNotNull null

            // Every additional key must also be present, either way
            val allKeysPresent = otherStringKeys.all { key ->
                result.method.containsKey(key, switchKeys[key])
            }
            if (!allKeysPresent) return@mapNotNull null

            JsonParserMatch(method = result.method, matchIndex = matchIndex, isStringMatch = false)
        }
    }

    private val normalFingerprint = Fingerprint(
        filters = listOf(string(mainStringKey)),
        name = "unsafeParseFromJson"
    )

    context(_: BytecodePatchContext)
    private fun findSwitchKeyForString(key: String): Int? {
        with(stringDictionaryMethod) {
            val offsetToSwitchKey = instructions
                .filterIsInstance<SwitchPayload>()
                .first()
                .switchElements
                .associate { it.offset to it.key }

            val stringLoadIndex = indexOfFirstStringInstruction(key)
            if (stringLoadIndex == -1) return null

            var codeUnitOffset = 0
            instructions.forEachIndexed { index, instruction ->
                if (index == stringLoadIndex) {
                    return offsetToSwitchKey.entries
                        .firstOrNull { (offset, _) -> offset in (codeUnitOffset - 2)..(codeUnitOffset + 2) }
                        ?.value
                }
                codeUnitOffset += instruction.codeUnits
            }

            return null
        }
    }


    companion object {
        private var _jsonParserFullList: List<Match>? = null

        context(_: BytecodePatchContext)
        private val jsonParserFullList: List<Match>
            get() = _jsonParserFullList
                ?: Fingerprint(name = "unsafeParseFromJson")
                    .matchAll().also { _jsonParserFullList = it }

        context(_: BytecodePatchContext)
        private val stringDictionaryMethod: MutableMethod
            get() = Fingerprint(
                definingClass = STRING_DICTIONARY_CLASS,
                name = STRING_DICTIONARY_METHOD
            ).method
    }
}


context(_: BytecodePatchContext)
fun MutableMethod.indexOfFirstDictionaryCall(switchKey: Int): Int? {
    if (implementation == null) return null

    for (index in 1 until instructions.size) {
        val ref = getInstruction(index).getReference<MethodReference>()
        if (ref?.name == STRING_DICTIONARY_METHOD && ref.definingClass == STRING_DICTIONARY_CLASS) {
            val literal = getInstruction<NarrowLiteralInstruction>(index - 1)
            if (literal.narrowLiteral == switchKey) return index - 1
        }
    }
    return null
}

context(_: BytecodePatchContext)
private fun MutableMethod.containsKey(stringKey: String, switchKey: Int?): Boolean =
    indexOfFirstInstruction { getReference<StringReference>()?.string == stringKey } >= 0
            || (switchKey != null && indexOfFirstDictionaryCall(switchKey) != null)
