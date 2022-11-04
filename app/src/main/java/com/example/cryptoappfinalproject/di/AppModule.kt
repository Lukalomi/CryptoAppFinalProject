package com.example.cryptoappfinalproject.di

import android.app.Application
import androidx.room.Room
import com.example.cryptoappfinalproject.common.CryptoEndPoints
import com.example.cryptoappfinalproject.common.ConversionEndPoint
import com.example.cryptoappfinalproject.common.NewsEndPoint
import com.example.cryptoappfinalproject.common.VideosEndPoint
import com.example.cryptoappfinalproject.data.local.CryptoLocalDatabase
import com.example.cryptoappfinalproject.data.remote.fetchApi.*
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
            Retrofit.Builder().baseUrl(CryptoEndPoints.BASE_URL).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedCrypto::class.java)

        @Singleton
        @Provides
        fun cryptoNews(): FetchedNews =
            Retrofit.Builder().baseUrl(NewsEndPoint.BASE_URL_NEWS).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedNews::class.java)


        @Singleton
        @Provides
        fun videoTitleData(): FetchVideoTitles =
            Retrofit.Builder().baseUrl(VideosEndPoint.BASE_URL_YTVIDEO).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchVideoTitles::class.java)



        @Singleton
        @Provides
        fun convertCrypto(): FetchedConvert =
            Retrofit.Builder().baseUrl(ConversionEndPoint.BASE_URL).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedConvert::class.java)


        @Singleton
        @Provides
        fun searchCrypto(): FetchedSearch =
            Retrofit.Builder().baseUrl(CryptoEndPoints.BASE_URL).client(providesOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FetchedSearch::class.java)




    }



//
//
//    @Singleton
//    @Provides
//    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
//


}