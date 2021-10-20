package com.moviessampleapp.data.network

import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Created by Nadeem on 20/10/2021.
 *
 * Custom interceptor to read from Okhttp3 [Cache] for 10 minutes.
 * This is added as a network interceptor to
 * [OkHttpClient.Builder.networkInterceptors] in NetworkModule di
 */
class CacheControlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
            .newBuilder()
            .header(
                CACHE_CONTROL_HEADER,
                CacheControl.Builder()
                    .maxAge(MAX_CACHE_AGE, TimeUnit.MINUTES)
                    .build()
                    .toString()
            ).build()
    }

    companion object {
        private const val CACHE_CONTROL_HEADER = "Cache-Control"
        private const val MAX_CACHE_AGE = 10 // 10 minutes
    }
}