package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ConversionEndPoint
import com.example.cryptoappfinalproject.domain.CryptoConverterModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedConversion {

    @GET(ConversionEndPoint.convertCoins)
    suspend fun convertCoins(@Query("from") from: String, @Query("to") to: String, @Query("amount") amount: String)
            : Response<CryptoConverterModel>


}