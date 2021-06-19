package com.manangatangy.musifesti.model

import com.google.gson.GsonBuilder
import com.manangatangy.musifesti.MusicFestivalsApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class Repository {
    private var client = RetrofitClient.MUSIC_FESTIVALS_SERVICE

    suspend fun getFestivals() = client.getFestivals()
}

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

// response structures
data class MusicFestival(
    val name: String? = null,
    val bands: List<Band>? = null
)

data class Band(
    val name: String? = null,
    val recordLabel: String = ""
)
