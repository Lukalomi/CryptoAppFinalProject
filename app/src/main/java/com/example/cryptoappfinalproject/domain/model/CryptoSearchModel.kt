package com.example.cryptoappfinalproject.domain.model

data class CryptoSearchModel(
    val coins: MutableList<Coin>?,
) {
    data class Coin(
        val name: String?,
        val marketCapRank: Int?,
        val thumb: String?,
    )
}