# movies-sample-app
This is an Android app which fetches a [sample movies list](https://movies-sample.herokuapp.com/api/movies) to display. 
Built using Kotlin and latest Android tech stack, with an approach to clean architecture.
## Functionality
* Search by movie genre and title, with InputFilter/SearchView.
* Request caching for fetching movies sample & images, for 10 minutes. This is implemented with a custom CacheControlInterceptor.
* Portrait/Landscape support - more item rows on landscape, less on portrait.
## Technologies & Patterns Used
* MVVM pattern with lifecycle components
* Data binding
* Coil for image loading
* Coroutines for asynchronous calls
* Okhttp3/Retrofit2 for network requests
* Gson for parsing
* Dagger-Hilt for dependency injection & also used in UI (Instrumentation tests)
* Mockito/io.mockk for unit tests
* Timber for logging
## Running the app/tests
This project should build & run fine on latest versions of Android Studio IDE.
To run tests -
* Unit Tests:
    * `./gradlew testDebugUnitTest` and/or `./gradlew testReleaseUnitTest`
    * You can also run the com.moviessampleapp **test** package directly from Android Studio.
* Automation Tests (run with a connected emulator or device):
    * `adb shell am instrument -w -r  --no-window-animation  -e package com.moviessampleapp -e debug false com.moviessampleapp.debug.test/com.moviessampleapp.HiltTestRunner`
    * You can also run the com.moviessampleapp.ui **androidTest** package directly from Android Studio.
## Screenshots & Videos
#### Portrait screen -
![movies-sample-app-portrait](https://user-images.githubusercontent.com/17089810/138202071-86c1efc3-40e8-45da-b74c-69a9e4e71d1c.png)
#### Landscape screen -
![movies-sample-app-landscape](https://user-images.githubusercontent.com/17089810/138202081-93c894f9-5864-47e2-9ea8-4f8d2a5b26cf.png)
#### Short video of the app -
https://user-images.githubusercontent.com/17089810/138203774-703f4f9e-5e16-45fd-8d28-0373f9f40942.mp4

