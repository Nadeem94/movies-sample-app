package com.moviessampleapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Nadeem on 19/10/2021.
 */
@HiltAndroidApp
class MoviesSampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}