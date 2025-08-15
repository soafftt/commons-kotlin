package soft.common.encoder

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data object UTF8Encoder {
    private val UTF_8_CHARSET: Charset = StandardCharsets.UTF_8

    fun encode(charSequence: CharSequence): ByteArray =
        try {
            val byteBuffer = UTF_8_CHARSET.newEncoder().encode(CharBuffer.wrap(charSequence))
            byteBuffer.array().copyOfRange(0, byteBuffer.limit())
        } catch (ex: CharacterCodingException) {
            throw IllegalArgumentException("Encoding failed", ex);
        }

    fun decode(byteArray: ByteArray): String =
        try {
            UTF_8_CHARSET.newDecoder()
                .decode(ByteBuffer.wrap(byteArray))
                .toString()
        } catch (ex: CharacterCodingException) {
            throw IllegalArgumentException("Decoding failed", ex);
        }
}