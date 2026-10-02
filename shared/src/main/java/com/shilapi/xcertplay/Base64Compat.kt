package com.shilapi.xcertplay

import kotlin.io.encoding.Base64

/** java.util.Base64 needs API 26; this works on Android 7. */
object Base64Compat {
    fun encode(bytes: ByteArray): String = Base64.Default.encode(bytes)
    fun decode(text: String): ByteArray = Base64.Default.decode(text)
    fun decodeMime(text: String): ByteArray = Base64.Mime.decode(text)
    fun decodeMime(bytes: ByteArray): ByteArray = Base64.Mime.decode(bytes)
    fun encodeLines(bytes: ByteArray, lineLength: Int, separator: String): String =
        Base64.Default.encode(bytes).chunked(lineLength).joinToString(separator)
}
