package com.example.cryptoappfinalproject.data.remote

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.domain.CryptoNewsModel
import com.example.cryptoappfinalproject.domain.VideoTitleModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FetchVideoTitles {

    @GET(ApiEndPoints.getFirstVideo)
    suspend fun getFirstVideoTitle() : Response<VideoTitleModel>
    @GET(ApiEndPoints.getSecondVideo)
    suspend fun getSecondVideoTitle() : Response<VideoTitleModel>
    @GET(ApiEndPoints.getThirdVideo)
    suspend fun getThirdVideoTitle() : Response<VideoTitleModel>


}