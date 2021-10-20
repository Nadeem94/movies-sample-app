package com.moviessampleapp.usecase

import com.moviessampleapp.data.Movie
import com.moviessampleapp.repository.MoviesRepository
import javax.inject.Inject

/**
 * Created by Nadeem on 19/10/2021.
 */
class MoviesSampleUseCase @Inject constructor(private val moviesRepository: MoviesRepository) {

    suspend fun invoke(): List<Movie> {
        return moviesRepository.retrieveMovies().sortedBy { it.title }
    }

}