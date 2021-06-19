package com.manangatangy.musifesti

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.manangatangy.musifesti.support.BaseTest
import com.manangatangy.musifesti.view.MusicFestivalsActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusicFestivalsActivityTest : BaseTest() {

    @Test
    fun testSuccessfulResponse() {
        setResponse("/codingtest/api/v1/festivals", 200,
            readTextFromFile("success_response.json"))

        ActivityScenario.launch(MusicFestivalsActivity::class.java)
        Thread.sleep(1000000)
    }

    @Test
    fun testFailR429esponse() {
        setResponse("/codingtest/api/v1/festivals", 429,
            "Too Many Requests")

        ActivityScenario.launch(MusicFestivalsActivity::class.java)
        Thread.sleep(1000000)
    }

}


/*
mockResponse.throttleBody(1024, 1, TimeUnit.SECONDS)
val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS) // For testing purposes
        .readTimeout(2, TimeUnit.SECONDS) // For testing purposes
        .writeTimeout(2, TimeUnit.SECONDS)
        .build()

 */