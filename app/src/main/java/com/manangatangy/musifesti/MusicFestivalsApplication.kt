package com.manangatangy.musifesti

import android.app.Application

open class MusicFestivalsApplication : Application() {

    open fun getApiBaseUrl() = BuildConfig.API_BASE_URL

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private lateinit var appInstance: MusicFestivalsApplication
        val instance: MusicFestivalsApplication get() = appInstance
    }

}
