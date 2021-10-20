package com.moviessampleapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.moviessampleapp.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Nadeem on 19/10/2021.
 */
@AndroidEntryPoint
class MoviesSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_sample_activity)

        supportFragmentManager.commitNow {
            replace(R.id.container, MoviesSampleFragment.newInstance())
        }
    }
}