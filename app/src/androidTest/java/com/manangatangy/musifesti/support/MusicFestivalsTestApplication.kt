package com.manangatangy.musifesti.support

import com.manangatangy.musifesti.MusicFestivalsApplication

class MusicFestivalsTestApplication : MusicFestivalsApplication() {
    // TODO Use Dagger to provide this data for testing,
    //  instead of subclassing the MusicFestivalsApplication
    override fun getApiBaseUrl(): String {
        return "http://127.0.0.1:8080"
    }
}
