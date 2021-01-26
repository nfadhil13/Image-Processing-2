package ImageProcessor.stenography

import ImageProcessor.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import util.BinaryUtil.getBinaryString
import util.BinaryUtil.getBit
import util.safeCall
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class StenograpyhEncode{


    fun run(
        message : String,
        filePath : String,
        stopCharacter : Char,
        dispatcher: CoroutineDispatcher
    ) : Flow<Resource<ByteArrayInputStream>> = safeCall(
        tobeExecute = {
            if(filePath.isNotBlank() && message.isNotBlank()){
                println("$message${stopCharacter.toString()}")
                val byteMessage = extraceMessageToByteArray("$message${stopCharacter.toString()}")
                var messageIndex = 0
                val src = Imgcodecs.imread(filePath)
                for (i in 0 until src.rows()) {
                    for (j in 0 until src.cols()) {
                        val data = src.get(i , j)
                        //Trying To Encode
                        if (messageIndex < byteMessage.length) {
                            for (x in data.indices) {
                                if(messageIndex < byteMessage.length){
                                    val oldByte: String = getBinaryString(data[x].toInt())
                                    val newByte = oldByte.substring(0, 7) + byteMessage[messageIndex]
                                    messageIndex++
                                    data[x] = newByte.toInt(2).toDouble()
                                    println("$oldByte vs $newByte jadi gini : ${data[x]}")
                                }
                            }
                        }
                        src.put(i, j, *data)
                    }
                }
                val buffer = MatOfByte()
                Imgcodecs.imencode(".png", src, buffer)
                ByteArrayInputStream(buffer.toArray())
            }else{
                throw Exception("Tidak boleh kosong")
            }
        },
        dispatcher = dispatcher,
        successMessage = "Berhasil melakukan encode stenography"
    )






    private fun extraceMessageToByteArray(message: String) : String{
            val testBytes: ByteArray = message.toByteArray(StandardCharsets.UTF_8)
            var byteMessage = ""
            for (i in 0 until 8 * message.length) {
                byteMessage += java.lang.String.valueOf(getBit(testBytes, i))
            }
            println(byteMessage)
            return byteMessage
    }

}