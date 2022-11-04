package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.CryptoEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.CryptoSearchModelDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedSearch {
    @GET(CryptoEndPoints.searchCoins)
    suspend fun searchCoins(@Query("query") query: String): CryptoSearchModelDto

}