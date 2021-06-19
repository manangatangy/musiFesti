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
}
