package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.CryptoNewsModelDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedNews {

    @GET(ApiEndPoints.getCryptoNews)
    suspend fun getNews(@Query("page")page:Int) : Response<MutableList<CryptoNewsModelDto.Data>>

}