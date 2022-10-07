package com.example.cryptoappfinalproject.di

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun apiService(): FetchedCrypto =
        Retrofit.Builder().baseUrl(ApiEndPoints.BASE_URL).client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FetchedCrypto::class.java)
}