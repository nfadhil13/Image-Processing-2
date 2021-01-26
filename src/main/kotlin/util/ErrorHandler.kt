package util

import kotlinx.coroutines.TimeoutCancellationException

fun errorHandler(exception: Exception) : String{
    var message = exception.message ?: "Error tidak diketahui , silahkan coba lagi"
    return message
}