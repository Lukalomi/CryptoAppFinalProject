package com.example.cryptoappfinalproject.domain.model
import com.google.gson.annotations.SerializedName


data class CryptoNewsModel(val data: List<Data>?) {
    data class Data(
        val newsUrl: String?,
        val imageUrl: String?,
        val title: String?,
        val text: String?,
        val date: String?
    )
}