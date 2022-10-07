package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import retrofit2.Response
import retrofit2.http.GET

interface FetchedCrypto {
    @GET(ApiEndPoints.searchCoins)
    suspend fun searchCoins() : Response<CryptoCoinsModel>


    @GET(ApiEndPoints.searchExchanges)
    suspend fun searchExchanges(): Response<CryptoExchangesModel>
}