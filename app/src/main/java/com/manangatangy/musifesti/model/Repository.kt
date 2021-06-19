package com.manangatangy.musifesti.model

import com.google.gson.GsonBuilder
import com.manangatangy.musifesti.MusicFestivalsApplication
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class Repository {
    private var client = RetrofitClient.MUSIC_FESTIVALS_SERVICE

    // Ref: https://proandroiddev.com/using-retrofit-2-with-kotlin-coroutines-cb112f0fb738
    suspend fun getFestivals(): ApiResult<List<MusicFestival>> =
        try {
            ApiResult.Ok(client.getFestivals())
        } catch (httpException: HttpException) {
            ApiResult.HttpError(httpException, httpException.response())
        } catch (exception: Throwable) {
            ApiResult.Error(exception)
        }
}

            /*
            httpException
                code  429
                message 'client error'
                response Response{protocol=http/1.1, code=429, message=Client Error, url=http://127.0.0.1:8080/codingtest/api/v1/festivals}
                    body null
                    errorBody (ResponseBody)
                        content '[text=Too Many Requests]
                        contentType null
                        contentLength 17

             */

object RetrofitClient  {
    private const val REQUEST_TIMEOUT = 3L

    val OK_HTTP_CLIENT: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
    val MUSIC_FESTIVALS_SERVICE: MusicFestivalsService by lazy {
        Retrofit.Builder()
            .baseUrl(MusicFestivalsApplication.instance.getApiBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(OK_HTTP_CLIENT)
            .build().create(MusicFestivalsService::class.java)
    }

    // retrofit2.HttpException: HTTP 429 Too Many Requests

    interface MusicFestivalsService {
        @GET("/codingtest/api/v1/festivals")
        suspend fun getFestivals(): List<MusicFestival>
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

// https://stackoverflow.com/questions/44298702/what-is-out-keyword-in-kotlin
sealed class ApiResult<out T> {
    class Ok<T>(val value: T): ApiResult<T>() {
        override fun toString() = "Result.Ok{value=$value}"
    }
    class HttpError<T>(val httpException: HttpException,
                       val response: Response<out T>?) : ApiResult<Nothing>() {
        override fun toString() = "Result.HttpError{httpException=$httpException}"
    }
    class Error(val exception: Throwable) : ApiResult<Nothing>() {
        override fun toString() = "Result.Error{exception=$exception}"
    }
}
