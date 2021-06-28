package com.manangatangy.musifesti

import android.app.Application
import com.manangatangy.musifesti.model.*
import okhttp3.OkHttpClient
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module

open class MusicFestivalsApplication : Application() {

    open val applicationModule = module {
        single<BaseUrlProvider> {
            object : BaseUrlProvider {
                override fun getBaseUrl(): String = BuildConfig.API_BASE_URL
            }
        }
        single<OkHttpClient> { OkHttpClientProviderImpl.getOkHttpClient() }
        single<ApiClient> { RetrofitClientImpl(get()) }
        factory<Repository> { Repository }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule))
    }

}
