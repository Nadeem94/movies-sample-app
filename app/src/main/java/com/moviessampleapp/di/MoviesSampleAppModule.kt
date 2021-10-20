package com.moviessampleapp.di

import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moviessampleapp.BuildConfig
import com.moviessampleapp.MoviesSampleApplication
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
class MoviesSampleAppModule {

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext context: Context): MoviesSampleApplication {
        return context as MoviesSampleApplication
    }

    @Provides
    @Singleton
    fun provideContext(app: MoviesSampleApplication): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun providesCoilImageLoader(
        context: Context,
        client: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .okHttpClient(client)
            .build()
    }
}