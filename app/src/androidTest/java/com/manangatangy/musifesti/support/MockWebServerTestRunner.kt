package com.manangatangy.musifesti.support

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

// A non-standard testInstrumentationRunner is needed to run the test version of the Application
// This can be removed (and androidx.test.runner.AndroidJUnitRunner re-instated) once Dagger is
// used to inject the api-base-url, needed for mock web server.
class MockWebServerTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, MusicFestivalsTestApplication::class.java.name, context)
    }
}
