package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoConverterModel
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedCrypto {
    @GET(ApiEndPoints.getCoins)
    suspend fun getCoins(@Query("page")page:Int) : Response<CryptoCoinsModel>

    @GET(ApiEndPoints.searchExchanges)
    suspend fun searchExchanges(): Response<CryptoExchangesModel>

    @GET(ApiEndPoints.searchCoins)
    suspend fun searchCoins(@Query("query") query: String):Response<CryptoSearchModel>

    @GET(ApiEndPoints.convertCoins)
    suspend fun convertCoins(@Query("ids") ids: String, @Query("vs_currencies") vsCurrencies: String, ) : Response<CryptoConverterModel>


}