package com.rodrigo.eventos.data.repository.network.helpers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rodrigo.eventos.data.model.ErrorResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NetworkHelper(private val gson: Gson) {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.Error(-1)
                    is HttpException -> ResultWrapper.Error(
                        throwable.code(),
                        convertErrorBody(throwable)
                    )
                    else -> ResultWrapper.Error(null, null)
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.charStream()?.let {
                gson.fromJson(it, object : TypeToken<ErrorResponse>() {}.type)
            }
        } catch (exception: Exception) {
            ErrorResponse(throwable.code(), throwable.response()?.errorBody()?.string(), null)
        }
    }
}