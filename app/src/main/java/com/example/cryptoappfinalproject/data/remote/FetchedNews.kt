package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.CryptoNewsModelDto
import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchedNews {

    @GET(ApiEndPoints.getCryptoNews)
    suspend fun getNews(@Query("page")page:Int) : Response<MutableList<CryptoNewsModelDto.Data>>

}