package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.domain.model.VideoTitleModel

interface VideoTitleRepository {

    suspend fun getVideoTitle() : VideoTitleModel

}