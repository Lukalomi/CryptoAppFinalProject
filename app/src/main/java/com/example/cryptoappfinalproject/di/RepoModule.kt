package com.example.cryptoappfinalproject.di

import com.example.cryptoappfinalproject.data.remote.repositoryImpl.*
import com.example.cryptoappfinalproject.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindCryptoConverterRepository(
        cryptoConverterRepositoryImpl: CryptoConverterRepositoryImpl
    ) : CryptoConverterRepository


    @Binds
    @Singleton
    abstract fun bindCryptoExchangeRepository(
        cryptoExchangesRepositoryImpl: CryptoExchangesRepositoryImpl
    ): CryptoExchangesRepository


    @Binds
    @Singleton
    abstract fun searchCryptoRepository(
        searchRepositoryImpl: CryptoSearchRepositoryImpl
    ): CryptoSearchRepository

//    @Binds
//    @Singleton
//    abstract fun provideLoginRepository(
//        loginRepositoryImpl: LoginRepositoryImpl
//    ): LoginRepository
//
//    @Binds
//    @Singleton
//    abstract fun provideRegisterRepository(
//        registerRepositoryImpl: RegisterRepositoryImpl
//    ): RegisterRepository

}