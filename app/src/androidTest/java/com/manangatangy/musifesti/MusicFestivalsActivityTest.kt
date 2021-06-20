package com.manangatangy.musifesti

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.manangatangy.musifesti.support.BaseTest
import com.manangatangy.musifesti.view.MusicFestivalsActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MusicFestivalsActivityTest : BaseTest() {

    @Test
    fun testSuccessfulResponse() {
        setResponse("/codingtest/api/v1/festivals", 200,
            readTextFromFile("success_response.json"))

        ActivityScenario.launch(MusicFestivalsActivity::class.java)

        onView(withId(R.id.rv_band_list)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_error_message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.bn_reload)).check(matches(isDisplayed()))
    }

    @Test
    fun testFailR429Response() {
        setResponse("/codingtest/api/v1/festivals", 429,
            "Too Many Requests")

        ActivityScenario.launch(MusicFestivalsActivity::class.java)

        onView(withId(R.id.rv_band_list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_error_message)).check(matches(isDisplayed()))
        onView(withId(R.id.bn_reload)).check(matches(isDisplayed()))
    }

    @Test
    fun testFailWithTimeout() {
        setResponse("/codingtest/api/v1/festivals", 200,
            readTextFromFile("success_response.json"),
            headerDelayMillis = 20000)

        ActivityScenario.launch(MusicFestivalsActivity::class.java)

        onView(withId(R.id.rv_band_list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_error_message)).check(matches(isDisplayed()))
        onView(withId(R.id.bn_reload)).check(matches(isDisplayed()))
    }
}
