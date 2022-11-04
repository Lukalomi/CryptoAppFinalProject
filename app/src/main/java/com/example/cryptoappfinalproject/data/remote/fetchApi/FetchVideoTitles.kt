package com.example.cryptoappfinalproject.data.remote.fetchApi

import com.example.cryptoappfinalproject.common.VideosEndPoint
import com.example.cryptoappfinalproject.data.remote.dto.VideoTitleModelDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchVideoTitles {

    @GET(VideosEndPoint.getFirstVideo)
    suspend fun getFirstVideoTitle() : Response<VideoTitleModelDto>
    @GET(VideosEndPoint.getSecondVideo)
    suspend fun getSecondVideoTitle() : Response<VideoTitleModelDto>
    @GET(VideosEndPoint.getThirdVideo)
    suspend fun getThirdVideoTitle() : Response<VideoTitleModelDto>


}