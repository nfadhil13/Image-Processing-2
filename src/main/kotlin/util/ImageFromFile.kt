package util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream
import java.io.File

fun imageFromFile(file: File): ImageBitmap {
    return org.jetbrains.skija.Image.makeFromEncoded(file.readBytes()).asImageBitmap()
}

fun imageFromByteInputStream(byteArrayInputStream: ByteArrayInputStream)  : ImageBitmap{
    println(byteArrayInputStream.readBytes().size)
    return org.jetbrains.skija.Image.makeFromEncoded(byteArrayInputStream.readBytes()).asImageBitmap()
}