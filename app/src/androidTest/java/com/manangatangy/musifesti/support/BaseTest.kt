package com.manangatangy.musifesti.support

import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.manangatangy.musifesti.model.RetrofitClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        // The need for the idling resource is explained here:
        // https://developer.android.com/training/testing/espresso/idling-resource
        // https://medium.com/insiden26/okhttp-idling-resource-for-espresso-462ef2417049
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                RetrofitClient.OK_HTTP_CLIENT
            )
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // Reads text files from musiFesti/app/src/androidTest/assets
    // (Bundled with the test-apk and not bundled with the app apk.)
    // Ref: https://stackoverflow.com/questions/9898634/how-to-provide-data-files-for-android-unit-tests
    fun readTextFromFile(fileName: String): String =
        InstrumentationRegistry.getInstrumentation()
            .context.resources.assets.open(fileName).use { inputStream ->
                inputStream.bufferedReader().readText()
            }

    // Set the mockWebServer to respond to the specified path.
    // Any other request will respond not-found
    // TODO Improve this to handle many request paths, using builder pattern
    fun setResponse(requestPath: String, responseCode: Int, responseBody: String) {
        mockWebServer.dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse? =
                when (request.path) {
                    requestPath -> MockResponse().setResponseCode(responseCode).setBody(responseBody)
                    else -> MockResponse().setResponseCode(404)
                }
        }
    }
}
