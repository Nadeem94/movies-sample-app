package com.moviessampleapp.data.network

import retrofit2.http.GET

/**
 * Created by Nadeem on 19/10/2021.
 */
interface MoviesSampleApi {

    @GET("/api/movies")
    suspend fun getMoviesSample(): MoviesSampleResponse

}