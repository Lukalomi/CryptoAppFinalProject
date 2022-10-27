package com.example.cryptoappfinalproject.domain.model

abstract class CryptoExchangesModel : MutableList<CryptoExchangesModel.CryptoExchangesModelItem> {
    data class CryptoExchangesModelItem(
        val name: String?,
        val image: String?,
        val trustScoreRank: Int?,
        val tradeVolume24hBtc: Double?,
        val tradeVolume24hBtcNormalized: Double?
    )
}