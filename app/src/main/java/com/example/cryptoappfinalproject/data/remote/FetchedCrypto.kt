package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.CryptoCoinsModelDto
import com.example.cryptoappfinalproject.data.remote.dto.CryptoExchangesModelDto
import com.example.cryptoappfinalproject.data.remote.dto.CryptoSearchModelDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedCrypto {
    @GET(ApiEndPoints.getCoins)
    suspend fun getCoins(@Query("page")page:Int) : Response<MutableList<CryptoCoinsModelDto.CryptoCoinsModelItem>>

    @GET(ApiEndPoints.searchExchanges)
    suspend fun searchExchanges(): Response<MutableList<CryptoExchangesModelDto.CryptoExchangesModelItem>>

    @GET(ApiEndPoints.searchCoins)
    suspend fun searchCoins(@Query("query") query: String):Response<MutableList<CryptoSearchModelDto.Coin>>



}