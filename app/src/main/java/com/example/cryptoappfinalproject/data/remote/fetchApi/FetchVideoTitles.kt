package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.ApiEndPoints
import com.example.cryptoappfinalproject.data.remote.dto.VideoTitleModelDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchVideoTitles {

    @GET(ApiEndPoints.getFirstVideo)
    suspend fun getFirstVideoTitle() : Response<VideoTitleModelDto>
    @GET(ApiEndPoints.getSecondVideo)
    suspend fun getSecondVideoTitle() : Response<VideoTitleModelDto>
    @GET(ApiEndPoints.getThirdVideo)
    suspend fun getThirdVideoTitle() : Response<VideoTitleModelDto>


}