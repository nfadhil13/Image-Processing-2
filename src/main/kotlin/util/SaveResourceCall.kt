package util

import ImageProcessor.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> safeCall(
    tobeExecute : suspend  () -> T,
    dispatcher: CoroutineDispatcher,
    successMessage : String,
) : Flow<Resource<T>> = flow{
    try{
        emit(Resource.Loading<T>())
        emit(Resource.Success(data = tobeExecute() , message = successMessage))
    }catch(exception : Exception){
        println(exception)
        emit(Resource.Error<T>(message = errorHandler(exception = exception)))

    }
}.flowOn(dispatcher)