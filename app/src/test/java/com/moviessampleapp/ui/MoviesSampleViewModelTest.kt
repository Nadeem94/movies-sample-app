package com.moviessampleapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moviessampleapp.data.Movie
import com.moviessampleapp.usecase.MoviesSampleUseCase
import io.mockk.coEvery
import io.mockk.mockkClass
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException

/**
 * Created by Nadeem on 21/10/2021.
 */
@ExperimentalCoroutinesApi
class MoviesSampleViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var dispatcher: TestCoroutineDispatcher
    private lateinit var useCase: MoviesSampleUseCase
    private lateinit var viewModel: MoviesSampleViewModel

    @Before
    fun setup() {
        dispatcher = TestCoroutineDispatcher()
        useCase = mockkClass(MoviesSampleUseCase::class)
        viewModel = MoviesSampleViewModel(useCase)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test search query changed posts search text`() {
        // given
        assertEquals(true, viewModel.onQueryChanged("Dunkirk"))
        // then
        assertEquals("Dunkirk", viewModel.searchText.value)
    }

    @Test
    fun `test search query changed allows empty string`() {
        // when
        assertEquals(true, viewModel.onQueryChanged(""))
        // then
        assertEquals("", viewModel.searchText.value)
    }

    @Test
    fun `test can retrieve empty sample movies list`() {
        // given
        dispatcher.runBlockingTest {
            coEvery { useCase.invoke() } coAnswers { emptyList() }
        }

        // when
        viewModel.retrieveSampleMovies()

        // then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(emptyList<Movie>(), viewModel.movies.value)
    }

    @Test
    fun `test retrieves list of sorted sample movies`() {
        // given
        dispatcher.runBlockingTest {
            coEvery { useCase.invoke() } coAnswers {
                TEST_MOVIE_LIST.sortedBy { it.title }
            }
        }

        // when
        viewModel.retrieveSampleMovies()

        // then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(2, viewModel.movies.value?.size)
        // Movie list should be sorted alphabetically by title -
        // so comparing where the item should be in actual live data list.
        assertEquals(TEST_MOVIE_LIST.component1(), viewModel.movies.value?.component2())
        assertEquals(TEST_MOVIE_LIST.component2(), viewModel.movies.value?.component1())
    }

    @Test
    fun `test retrieving sample movie list throws exception`() {
        // given
        dispatcher.runBlockingTest {
            coEvery { useCase.invoke() } coAnswers {
                throw SocketTimeoutException("Error, timed out while performing request")
            }
        }
        // when
        viewModel.retrieveSampleMovies()

        // then
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(
            "Error, timed out while performing request",
            viewModel.errorMessage.value
        )
    }

    companion object {

        private val TEST_MOVIE_LIST = listOf(
            Movie(
                id = 11241,
                title = "Jumanji: welcome to the jungle",
                genre = "Action",
                year = "2017",
                poster = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/bXrZ5iHBEjH7WMidbUDQ0U2xbmr.jpg"
            ),
            Movie(
                id = 37344,
                title = "John Wick",
                genre = "Action",
                year = "2014",
                poster = "https://image.tmdb.org/t/p/w370_and_h556_bestv2/5vHssUeVe25bMrof1HyaPyWgaP.jpg"
            )
        )
    }
}