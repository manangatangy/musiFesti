# musiFesti - Music Festival listing app

<img width="416" alt="Screen Shot 2021-06-20 at 8 09 21 pm" src="https://user-images.githubusercontent.com/85463120/122670126-a9c61d00-d203-11eb-8e0c-0ce3933e1e1f.png">
This is a demonstration app, developed as a response to the EnergyAustralia Coding Test 
https://eacp.energyaustralia.com.au/codingtest

## Features
 - Supports http/network errors from the api request
 - Reloads api data on user request
 - Uses Android LiveData/ViewModel and Kotlin coroutines to implement MVVM
 - Makes use of Retrofit2's native support for coroutines 
 - Several unit test and UI test cases

## Building
 - `git clone git@github.com:manangatangy/musiFesti.git`
 - Build/install/run using Android Studio or using gradle command line as follows
1. `cd musiFesti`
2. `./gradlew assembleDebug`
3. `./gradlew testDebugUnitTest`          *Run the unit tests*
4. `emulator -avd Pixel_XL_API_29`        *Start your emulator or connect your device*
5. `./gradlew connectedDebugAndroidTest`  *Run the instrumentation tests on connected device*
6. `./gradlew installDebug`               *Install the debug app*
7. `adb shell am start -a android.intent.action.MAIN -n com.manangatangy.musifesti/.view.MusicFestivalsActivity`
