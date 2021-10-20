package com.moviessampleapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviessampleapp.data.Movie
import com.moviessampleapp.usecase.MoviesSampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Nadeem on 19/10/2021.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesSampleUseCase: MoviesSampleUseCase,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    val movies = MutableLiveData<List<Movie>>()
    val searchText = MutableLiveData<String?>(null)

    fun retrieveSampleMovies() {
        viewModelScope.launch {
            runCatching {
                Timber.i("Retrieving sample movies...")
                moviesSampleUseCase.invoke()
            }.onSuccess {
                Timber.i("Successfully retrieved sample movies: $it")
                movies.postValue(it)
            }.onFailure {
                Timber.e(it, "Failed to retrieve sample movies.")
            }
        }
    }

    fun onQueryChanged(query: String): Boolean {
        searchText.postValue(query)
        return true
    }
}