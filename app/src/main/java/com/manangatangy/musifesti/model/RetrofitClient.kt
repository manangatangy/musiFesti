package com.manangatangy.musifesti.model

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit

private const val CALL_TIMEOUT = 10L

class RetrofitClient(baseUrlProvider: BaseUrlProvider): ApiClient, KoinComponent {

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
            .baseUrl(baseUrlProvider.getBaseUrl())
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
