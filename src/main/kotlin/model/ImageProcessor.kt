package model


sealed class ImageProcessor(
    val imagePath : String,
    val name : String
){
    class Stenography() : ImageProcessor("stenography.png" , "Stenograpy")
    class Noise() : ImageProcessor("noise.png" , "Noise")
    class Watermark() : ImageProcessor("watermark.png" , "Watermark")
}