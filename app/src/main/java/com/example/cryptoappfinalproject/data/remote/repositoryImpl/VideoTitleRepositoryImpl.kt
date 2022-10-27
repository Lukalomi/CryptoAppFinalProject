package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.FetchVideoTitles
import com.example.cryptoappfinalproject.data.remote.dto.toVideoTitleModel
import com.example.cryptoappfinalproject.domain.model.VideoTitleModel
import com.example.cryptoappfinalproject.domain.repository.VideoTitleRepository
import javax.inject.Inject

class VideoTitleRepositoryImpl @Inject constructor(
    private val fetchVideoTitles: FetchVideoTitles
): VideoTitleRepository {
    override suspend fun getVideoTitle(): VideoTitleModel {
        val response = fetchVideoTitles.getFirstVideoTitle()

        if (response.isSuccessful) {
            return response.body()!!.toVideoTitleModel()
        } else {
            return VideoTitleModel(1,"", "", "")
        }
    }

}