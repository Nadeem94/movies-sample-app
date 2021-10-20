package com.moviessampleapp.di

import android.content.Context
import coil.ImageLoader
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moviessampleapp.BuildConfig
import com.moviessampleapp.MoviesSampleApplication
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
    internal fun provideCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://movies-sample.herokuapp.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesSampleApi {
        return retrofit.create(MoviesSampleApi::class.java)
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