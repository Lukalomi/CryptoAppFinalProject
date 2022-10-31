package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.ConversionEndPoint
import com.example.cryptoappfinalproject.data.remote.dto.CryptoConverterModelDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedConvert {

    @GET(ConversionEndPoint.convertCoins)
    suspend fun convertCoins(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String)
            : Response<CryptoConverterModelDto>


}