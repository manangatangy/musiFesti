package com.manangatangy.musifesti.model

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

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

interface ApiClient {
    suspend fun getFestivals(): List<MusicFestival>
}

interface BaseUrlProvider {
    fun getBaseUrl(): String
}

private const val CALL_TIMEOUT = 10L

class RetrofitClientImpl(BaseUrlProvider: BaseUrlProvider): ApiClient, KoinComponent  {

    private val okHttpClient: OkHttpClient by inject()

    // retrofit >2.6.0 supports coroutines directly
    // Ref: https://proandroiddev.com/using-retrofit-2-with-kotlin-coroutines-cb112f0fb738
    override suspend fun getFestivals(): List<MusicFestival> = musicFestivalsService.getFestivals()

    interface MusicFestivalsApi {
        @Headers("Cache-Control: no-cache")
        @GET("/codingtest/api/v1/festivals")
        suspend fun getFestivals(): List<MusicFestival>
    }

    val musicFestivalsService: MusicFestivalsApi by lazy {
        Log.d("Repository", "new Retrofit created")
        Retrofit.Builder()
            .baseUrl(BaseUrlProvider.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build().create(MusicFestivalsApi::class.java)
    }
}

interface OkHttpClientProvider {
    fun getOkHttpClient(): OkHttpClient
}

object OkHttpClientProviderImpl: OkHttpClientProvider {
    override fun getOkHttpClient(): OkHttpClient {
        Log.d("Repository", "new OkHttpClient created")
        return OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .cache(null)
            .build()
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
