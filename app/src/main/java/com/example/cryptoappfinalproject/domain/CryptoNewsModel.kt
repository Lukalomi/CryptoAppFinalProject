package com.example.cryptoappfinalproject.domain
import com.google.gson.annotations.SerializedName


data class CryptoNewsModel(
    val `data`: List<Data>?,
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