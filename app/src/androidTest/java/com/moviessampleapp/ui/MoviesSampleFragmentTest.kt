package com.moviessampleapp.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.gson.Gson
import com.moviessampleapp.R
import com.moviessampleapp.data.Movie
import com.moviessampleapp.data.TestData
import com.moviessampleapp.data.network.MoviesSampleResponse
import com.moviessampleapp.launchFragmentInHiltContainer
import com.moviessampleapp.util.CustomViewActions.clickItemOnMoviesList
import com.moviessampleapp.util.CustomViewActions.typeSearchViewText
import com.moviessampleapp.util.DataBindingIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Created by Nadeem on 20/10/2021.
 */
@ExperimentalCoroutinesApi
@HiltAndroidTest
class MoviesSampleFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var okHttpClient: OkHttpClient

    lateinit var testData: List<Movie>
    lateinit var testFrag: MoviesSampleFragment
    lateinit var idlingResource: DataBindingIdlingResource

    @Before
    fun init() {
        hiltRule.inject()
        launchFragmentInHiltContainer<MoviesSampleFragment> { testFrag = this }
        okHttpClient.dispatcher.cancelAll()
        testData = gson.fromJson(TestData.moviesSampleJson, MoviesSampleResponse::class.java)
            .data.sortedBy { it.title }
        testFrag.moviesAdapter.setMovieList(testData)
        idlingResource = DataBindingIdlingResource(testFrag)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun destroy() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testSingleItemSearch() {
        assertEquals(testData.size, testFrag.moviesAdapter.itemCount)

        onView(withId(R.id.movies_search)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText("Movies Sample App")).check(matches(isDisplayed()))

        typeSearchViewText("dunkirk")
        clickItemOnMoviesList(0)

        //Check toast is correctly displayed after click on recyclerview
        onView(withText("Dunkirk (2017)")).check(matches(isDisplayed()))
        //Only 1 item in the list after filtering
        assertEquals(1, testFrag.moviesAdapter.itemCount)
    }

    @Test
    fun testSingleItemSearchThenClear() {
        assertEquals(testData.size, testFrag.moviesAdapter.itemCount)

        onView(withId(R.id.movies_search)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withText("Movies Sample App")).check(matches(isDisplayed()))

        typeSearchViewText("Power Rangers")
        clickItemOnMoviesList(0)

        //Check toast is correctly displayed after click on recyclerview
        onView(withText("Power Rangers (2017)")).check(matches(isDisplayed()))
        //Only 1 item in the list after filtering
        assertEquals(1, testFrag.moviesAdapter.itemCount)

        typeSearchViewText("")

        //Full list after clearing search term
        clickItemOnMoviesList(testData.size - 1)

        assertEquals(testData.size, testFrag.moviesAdapter.itemCount)
        // Check last item on list
        onView(withText("Wolves (2014)")).check(matches(isDisplayed()))
    }

    @Test
    fun testHistoryGenreSearch() {
        testGenre("History")
        onView(withText("Dunkirk (2017)")).check(matches(isDisplayed()))
    }

    @Test
    fun testActionGenreSearch() {
        testGenre("Action")
        onView(withText("Atomic Blonde (2017)")).check(matches(isDisplayed()))
    }

    @Test
    fun testSciFiGenreSearch() {
        testGenre("Sci-Fi")
        onView(withText("Guardians of the Galaxy (2014)")).check(matches(isDisplayed()))
    }

    @Test
    fun testFantasyGenreSearch() {
        testGenre("Fantasy")
        onView(withText("Conan the Barbarian (2011)")).check(matches(isDisplayed()))
    }

    @Test
    fun testAnimationGenreSearch() {
        testGenre("Animation")
        onView(withText("Coco (2017)")).check(matches(isDisplayed()))
    }

    @Test
    fun testAdventureGenreSearch() {
        testGenre("Adventure")
        onView(withText("Fantastic Beasts and Where to Find Them (2016)")).check(matches(isDisplayed()))
    }

    @Test
    fun testMysteryGenreSearch() {
        testGenre("Mystery")
        onView(withText("Blade Runner 2049 (2017)")).check(matches(isDisplayed()))
    }

    @Test
    fun testDramaGenreSearch() {
        testGenre("Drama")
        onView(withText("Abattoir (2016)")).check(matches(isDisplayed()))
    }

    @Test
    fun testEmptyMoviesList() {
        testFrag.moviesAdapter.setMovieList(emptyList())
        typeSearchViewText("The Mortal Instruments: City of Bones")
        onView(withText("The Mortal Instruments: City of Bones (2013)")).check(doesNotExist())
    }

    private fun testGenre(genre: String) {
        typeSearchViewText(genre)
        clickItemOnMoviesList(0)
        val expectedListSize = testData.filter { it.genre == genre }.size
        assertEquals(expectedListSize, testFrag.moviesAdapter.itemCount)
    }
}