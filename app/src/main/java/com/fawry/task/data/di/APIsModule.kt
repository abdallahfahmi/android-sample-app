package com.fawry.task.data.di

import com.fawry.task.data.network.APIsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIsModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideAPIsService(okHttpClient: OkHttpClient) = Retrofit.Builder().apply {
        baseUrl("https://api.themoviedb.org/3/")
        addConverterFactory(GsonConverterFactory.create())
        addConverterFactory(ScalarsConverterFactory.create())
        client(okHttpClient)
    }.build().create(APIsService::class.java)

}