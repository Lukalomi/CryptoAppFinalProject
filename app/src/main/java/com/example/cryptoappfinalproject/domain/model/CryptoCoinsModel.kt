package com.example.cryptoappfinalproject.domain.model

abstract class CryptoCoinsModel : MutableList<CryptoCoinsModel.CryptoCoinsModelItem> {
    data class CryptoCoinsModelItem(
        val name: String?,
        val image: String?,
        val currentPrice: Double?,
        val marketCapRank: Int?,
        val priceChangePercentage24h: Double?,
    )
}