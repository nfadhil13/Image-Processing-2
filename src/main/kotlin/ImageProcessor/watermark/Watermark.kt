package ImageProcessor.watermark

import ImageProcessor.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import util.BinaryUtil
import util.safeCall
import java.io.ByteArrayInputStream
import java.lang.Exception

class Watermark{


    fun run(
        opacity : Double = (10.0/100.0),
        baseImage : String,
        watermarkImage : String,
        dispatcher: CoroutineDispatcher
    ) : Flow<Resource<ByteArrayInputStream>> = safeCall(
        tobeExecute =  {
            val base = Imgcodecs.imread(baseImage)
            val watermark = Imgcodecs.imread(watermarkImage)
            val isBaseBigger = isImage1BiggerThanImage2(base.size() , watermark.size())
            if(!isBaseBigger){
              throw Exception("Base Image Should be bigger than watermark image")
            }
            val final = base.clone()
            val result = preprocessing(final , watermark)
            val buffer = MatOfByte()
            Imgcodecs.imencode(".png", result, buffer)
            ByteArrayInputStream(buffer.toArray())
        },
        dispatcher = dispatcher,
        successMessage = "Berhasil menerapkan watermark"
    )


    private fun preprocessing(baseImage: Mat , watermarkImage: Mat) : Mat{
        for (i in 0 until watermarkImage.rows()) {
            for (j in 0 until watermarkImage.cols()) {
                val data = watermarkImage.get(i, j)
                val baseData = baseImage.get(i , j)
                for(x in data.indices){
                    val alpha = data[x] / 225.0
                    baseData [x] = alpha * data[x] + (1- alpha) * baseData[x]
                }
                baseImage.put(i, j, *baseData)
            }
        }
        return baseImage
    }

    private fun isImage1BiggerThanImage2(image1Size : Size , image2Size : Size ) : Boolean{
        return (image1Size.height > image2Size.height && image1Size.width > image2Size.width)
    }


}