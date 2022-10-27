package com.example.cryptoappfinalproject.data.remote.dto

import com.example.cryptoappfinalproject.domain.model.VideoTitleModel
import com.google.gson.annotations.SerializedName

class VideoTitleModelDto(
    val id:Int?,
    val title: String?,
    @SerializedName("author_name")
    val authorName: String?,
    @SerializedName("author_url")
    val authorUrl: String?,
    val type: String?,
    val height: Int?,
    val width: Int?,
    val version: String?,
    @SerializedName("provider_name")
    val providerName: String?,
    @SerializedName("provider_url")
    val providerUrl: String?,
    @SerializedName("thumbnail_height")
    val thumbnailHeight: Int?,
    @SerializedName("thumbnail_width")
    val thumbnailWidth: Int?,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,
    val html: String?,
    val videoUrl: String?
)

    fun VideoTitleModelDto.toVideoTitleModel() : VideoTitleModel {
        return VideoTitleModel(
            id = id,
            title = title,
            thumbnailUrl = thumbnailUrl,
            videoUrl = videoUrl
        )
    }