package com.example.cryptoappfinalproject.domain.model


data class CryptoNewsModel(
    val data: List<Data>?
    ) {
    data class Data(
        val newsUrl: String?,
        val imageUrl: String?,
        val title: String?,
        val text: String?,
        val date: String?
    )
}