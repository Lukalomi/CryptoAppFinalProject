package com.example.cryptoappfinalproject.di

import android.app.Application
import androidx.room.Room
import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.local.CryptoLocalDatabase
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
    }
}