package com.moviessampleapp.repository

import com.moviessampleapp.data.Movie
import com.moviessampleapp.data.network.MoviesSampleApi
import javax.inject.Inject

/**
 * Created by Nadeem on 19/10/2021.
 */
class MoviesRepository @Inject constructor(private val api: MoviesSampleApi) {

    suspend fun retrieveMovies(): List<Movie> {
        return api.getMoviesSample().data
    }
}