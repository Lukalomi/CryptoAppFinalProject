package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.CryptoEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.CryptoCoinsModelDto
import com.example.cryptoappfinalproject.data.remote.dto.CryptoExchangesModelDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedCrypto {
    @GET(CryptoEndPoints.getCoins)
    suspend fun getCoins(@Query("page")page:Int) : Response<MutableList<CryptoCoinsModelDto.CryptoCoinsModelItem>>

    @GET(CryptoEndPoints.getExchanges)
    suspend fun getExchanges(): Response<MutableList<CryptoExchangesModelDto.CryptoExchangesModelItem>>


}