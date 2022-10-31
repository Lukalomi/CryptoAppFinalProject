package com.example.cryptoappfinalproject.di

import android.app.Application
import androidx.room.Room
import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.common.ConversionEndPoint
import com.example.cryptoappfinalproject.data.local.CryptoLocalDatabase
import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchVideoTitles
import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedConvert
import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedNews
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


    @Module
    @InstallIn(SingletonComponent::class)
    object AppDBModule {

        @Provides
        @Singleton
        fun provideRoomDB(app: Application) =
            Room.databaseBuilder(
                app,
                CryptoLocalDatabase::class.java, "crypto-final"
            ).fallbackToDestructiveMigration().build()

        @Provides
        @Singleton
        fun provideUserDao(db: CryptoLocalDatabase) = db.CryptoDao()


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

        @Singleton
        @Provides
        fun cryptoNews(): FetchedNews =
            Retrofit.Builder().baseUrl(ApiEndPoints.BASE_URL_NEWS).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedNews::class.java)


        @Singleton
        @Provides
        fun videoTitleData(): FetchVideoTitles =
            Retrofit.Builder().baseUrl(ApiEndPoints.BASE_URL_YTVIDEO).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchVideoTitles::class.java)



        @Singleton
        @Provides
        fun convertCrypto(): FetchedConvert =
            Retrofit.Builder().baseUrl(ConversionEndPoint.BASE_URL).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedConvert::class.java)

    }
}