package soft.commons.util

class EncodingUtil {
    companion object {
        fun concatenate(byteArray: Array<ByteArray>): ByteArray {
            val newByteArray: ByteArray = ByteArray(byteArray.sumOf { it.size })
            var destPos: Int = 0

            byteArray.forEach {
                System.arraycopy(it, 0, newByteArray, destPos, it.size)
                destPos += destPos + it.size
            }

            return newByteArray
        }

        fun subArray(array: ByteArray, beginIndex: Int, endIndex: Int): ByteArray = array.copyOfRange(beginIndex, endIndex)
    }
}


