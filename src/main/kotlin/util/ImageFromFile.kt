package util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

fun imageFromFile(file: File): ImageBitmap {
    return org.jetbrains.skija.Image.makeFromEncoded(file.readBytes()).asImageBitmap()
}

fun imageFromByteInputStream(byteArrayInputStream: ByteArrayInputStream)  : ImageBitmap{
    val newInputStream = ByteArrayOutputStream()
    byteArrayInputStream.copyTo(newInputStream)
    return org.jetbrains.skija.Image.makeFromEncoded(newInputStream.toByteArray()).asImageBitmap()
}