package com.manangatangy.musifesti.model

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

interface ApiClient {
    suspend fun getFestivals(): List<MusicFestival>
}

interface BaseUrlProvider {
    fun getBaseUrl(): String
}

object Repository: KoinComponent {
    private val apiClient: ApiClient by inject()

    suspend fun getFestivals(): ApiResult<List<MusicFestival>> {
        Log.d("Repository", "calling OkHttpClient.getFestivals()")
        return try {
            ApiResult.Success(apiClient.getFestivals())
        } catch (httpException: HttpException) {
            ApiResult.HttpError(httpException, httpException.response())
        } catch (exception: Throwable) {
            ApiResult.Error(exception)
        }
    }
}

// api response structures
data class MusicFestival(
    val name: String? = null,
    val bands: List<Band>? = null
)

data class Band(
    val name: String? = null,
    val recordLabel: String = ""
)

sealed class ApiResult<out T> {
    object Loading : ApiResult<Nothing>() {
        override fun toString() = "Result.Loading"
    }
    class Success<T>(val value: T): ApiResult<T>() {
        override fun toString() = "Result.Success{value=$value}"
    }
    class Error(val exception: Throwable) : ApiResult<Nothing>() {
        override fun toString() = "Result.Error{exception=$exception}"
    }
    class HttpError<T>(val httpException: HttpException,
                       val response: Response<out T>?) : ApiResult<Nothing>() {
        override fun toString() = "Result.HttpError{httpException=$httpException}"
    }
}
