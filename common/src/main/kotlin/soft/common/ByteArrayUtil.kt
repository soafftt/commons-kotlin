package soft.common

fun Array<ByteArray>.concatenate(): ByteArray {
    val newByteArray: ByteArray = ByteArray(this.sumOf { it.size })
    var destPos: Int = 0

    this.forEach {
        System.arraycopy(it, 0, newByteArray, destPos, it.size)
        destPos += destPos + it.size
    }

    return newByteArray
}

fun ByteArray.subArray(beginIndex: Int, endIndex: Int): ByteArray =
    this.copyOfRange(beginIndex, endIndex)