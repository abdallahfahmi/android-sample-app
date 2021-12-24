package com.fawry.task.data.di

import android.content.Context
import com.fawry.task.data.managers.NetworkConnectionManager
import com.fawry.task.data.network.APIsService
import com.fawry.task.data.network.interceptors.InternetConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionManager(@ApplicationContext context: Context) =
        NetworkConnectionManager(context)

    @Provides
    @Singleton
    fun provideHttpClient(connectionManager: NetworkConnectionManager): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(InternetConnectionInterceptor(connectionManager))
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideAPIsService(okHttpClient: OkHttpClient) = Retrofit.Builder().apply {
        baseUrl("https://api.themoviedb.org/3/")
        addConverterFactory(GsonConverterFactory.create())
        client(okHttpClient)
    }.build().create(APIsService::class.java)

}