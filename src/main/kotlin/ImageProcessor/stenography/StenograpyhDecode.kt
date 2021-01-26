package ImageProcessor.stenography

import ImageProcessor.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.opencv.imgcodecs.Imgcodecs
import util.BinaryUtil.getBinaryString
import util.BinaryUtil.getStringFromBinary
import util.safeCall

class StenograpyhDecode {



    fun run(
        imgFilePath : String,
        stopChar : Char,
        dispatcher: CoroutineDispatcher ,
    ): Flow<Resource<String>> = safeCall(
        tobeExecute =  {
            println(stopChar)
            val src = Imgcodecs.imread(imgFilePath)
            var decodedMessage = StringBuilder()
            var currentMessage = StringBuilder()
            for (i in 0 until src.rows()) {
                for (j in 0 until src.cols()) {
                    val data: DoubleArray = src.get(i, j)
                    for (x in data.indices) {
                        val oldByte: String = getBinaryString(data[x].toInt())
                        currentMessage.append(oldByte[7])
                        if (currentMessage.length == 8) {
                            val newChar: String = getStringFromBinary(currentMessage.toString())
                            println(newChar)
                            currentMessage = if (newChar ==  stopChar.toString()) {
                                break
                            } else {
                                decodedMessage.append(newChar)
                                StringBuilder()
                            }
                        }
                    }
                }
            }
            decodedMessage.toString()
        },
        dispatcher = dispatcher,
        successMessage = "Berhasil mendapatkan pesan rahasia"
    )



}