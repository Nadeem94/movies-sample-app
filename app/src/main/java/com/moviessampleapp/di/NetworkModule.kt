package com.moviessampleapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moviessampleapp.BuildConfig
import com.moviessampleapp.data.network.CacheControlInterceptor
import com.moviessampleapp.data.network.MoviesSampleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

/**
 * Created by Nadeem on 20/10/2021.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir.absolutePath, CACHE_NAME), CACHE_SIZE)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(CacheControlInterceptor())
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesSampleApi {
        return retrofit.create(MoviesSampleApi::class.java)
    }

    companion object {
        private const val BASE_URL = "https://movies-sample.herokuapp.com/"
        private const val CACHE_NAME = "HttpCache"
        private const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
    }
}