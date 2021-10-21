# movies-sample-app
Android app which fetches a sample [movies list](https://movies-sample.herokuapp.com/api/movies) to display. 
Built using Kotlin and latest Android tech stack, with an approach to clean architecture.
## Technologies & Patterns Used
* MVVM pattern with lifecycle components
* Data binding
* Coil for image loading
* Coroutines for asynchronous calls
* Okhttp3/Retrofit2 for network requests
* Gson for parsing
* dagger-hilt for dependency injection & also used in UI (Instrumentation tests)
* mockk/Mockito for unit tests
* Timber for logging
## Running the app/tests
This project should build & run fine on latest versions of Android Studio IDE.
To run tests:
* Unit Tests:
    * `<./gradlew testDebugUnitTest>` and/or `<./gradlew testReleaseUnitTest>`
    * You can also run the com.moviessampleapp **test** package directly from Android Studio
* Automation Tests:
    * `<adb shell am instrument -w -r  --no-window-animation  -e package com.moviessampleapp -e debug false com.moviessampleapp.debug.test/com.moviessampleapp.HiltTestRunner>` With a connected emulator or device
    * You can also run the com.moviessampleapp.ui **androidTest** package directly from Android Studio, with a connected emulator or device

