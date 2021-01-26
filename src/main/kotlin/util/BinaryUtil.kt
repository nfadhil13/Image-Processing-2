package util

object BinaryUtil {


   fun getBinaryString(input: Int): String {
        val tempResult = StringBuilder(Integer.toBinaryString(input))
        while (tempResult.toString().length < 8) {
            tempResult.insert(0, "0")
        }
        return tempResult.toString()
    }

    fun getBit(data: ByteArray, pos: Int): Int {
        val posByte = pos / 8
        val posBit = pos % 8
        val valByte = data[posByte]
        val valInt: Int = valByte.toInt() shr 8 - (posBit + 1) and 0x0001
        println(valInt)
        return valInt
    }


    fun getStringFromBinary(s: String): String {
        var str = ""
        for (i in 0 until s.length / 8) {
            val a = s.substring(8 * i, (i + 1) * 8).toInt(2)
            str += a.toChar()
        }
        return str
    }
}


public inline infix fun Byte.and(other: Byte): Byte = (this.toInt() and other.toInt()).toByte()