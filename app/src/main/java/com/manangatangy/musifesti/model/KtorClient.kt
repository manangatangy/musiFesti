package com.manangatangy.musifesti.model

import android.util.Log
import com.manangatangy.musifesti.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*


private const val TIME_OUT = 10_000

class KtorClientImpl(baseUrlProvider: BaseUrlProvider): ApiClient {

    val baseUrl =baseUrlProvider.getBaseUrl()

    override suspend fun getFestivals(): List<MusicFestival> {
        val requestUrl = "${baseUrl}/codingtest/api/v1/festivals"
        return httpClient.get(requestUrl)
    }

    private val httpClient: HttpClient by lazy {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
                engine {
                    connectTimeout = TIME_OUT
                    socketTimeout = TIME_OUT
                }
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) { Log.v("Logger =>", message) }
                }
                level = LogLevel.ALL
            }
            install(ResponseObserver) {
                onResponse { response -> Log.d("HTTP status:", "${response.status.value}") }
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}
