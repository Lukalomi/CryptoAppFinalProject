package com.example.cryptoappfinalproject.domain
import com.google.gson.annotations.SerializedName


data class VideoTitleModel(
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