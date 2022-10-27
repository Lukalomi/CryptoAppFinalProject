package com.example.cryptoappfinalproject.data.remote.dto

import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel
import com.google.gson.annotations.SerializedName

class CryptoNewsModelDto(
    val `data`: MutableList<Data>?,
    @SerializedName("total_pages")
    val totalPages: Int?
) {
    data class Data(
        @SerializedName("news_url")
        val newsUrl: String?,
        @SerializedName("image_url")
        val imageUrl: String?,
        val title: String?,
        val text: String?,
        @SerializedName("source_name")
        val sourceName: String?,
        val date: String?,
        val topics: List<String?>?,
        val sentiment: String?,
        val type: String?
    )
}

    fun CryptoNewsModelDto.Data.toCryptoNewsModel() : CryptoNewsModel.Data {
        return  CryptoNewsModel.Data (
            newsUrl = newsUrl,
            text = text,
            date = date,
            title = title,
            imageUrl = imageUrl
        )
    }