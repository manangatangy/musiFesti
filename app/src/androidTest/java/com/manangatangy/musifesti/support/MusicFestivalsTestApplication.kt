package com.manangatangy.musifesti.support

import com.manangatangy.musifesti.MusicFestivalsApplication
import com.manangatangy.musifesti.model.*
import okhttp3.OkHttpClient
import org.koin.dsl.module.module

class MusicFestivalsTestApplication : MusicFestivalsApplication() {

    override val applicationModule = module {
        single<BaseUrlProvider> {
            object : BaseUrlProvider {
                override fun getBaseUrl(): String = "http://127.0.0.1:8080"
            }
        }
        single<OkHttpClient> { OkHttpClientProviderImpl.getOkHttpClient() }
        single<ApiClient> { RetrofitClient(get()) }
        factory<Repository> { Repository }
    }

}
