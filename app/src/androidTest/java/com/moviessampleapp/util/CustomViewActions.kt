package com.moviessampleapp.util

import android.view.View
import android.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.moviessampleapp.R
import com.moviessampleapp.ui.adapter.MoviesAdapter
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/**
 * Created by Nadeem on 21/10/2021.
 */
object CustomViewActions {

    fun typeSearchViewText(text: String) {
        onView(ViewMatchers.withId(R.id.movies_search)).perform(
            object : ViewAction {
                override fun getDescription(): String {
                    return "Change view text"
                }

                override fun getConstraints(): Matcher<View> {
                    return CoreMatchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.isAssignableFrom(SearchView::class.java)
                    )
                }

                override fun perform(uiController: UiController?, view: View?) {
                    (view as SearchView).setQuery(text, true)
                }
            }
        )
    }

    fun clickItemOnMoviesList(pos: Int) {
        onView(ViewMatchers.withId(R.id.movies_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<MoviesAdapter.MovieViewHolder>(
                    pos, ViewActions.click()
                )
            )
    }
}