package com.example.cryptoappfinalproject.data.remote.dto

import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.google.gson.annotations.SerializedName

class CryptoSearchModelDto(
    val coins: MutableList<Coin>?,
    val exchanges: MutableList<Exchange?>?,
    val icos: List<Any?>?,
    val categories: List<Category?>?,
    val nfts: List<Nft?>?
) {
    data class Coin(
        val id: String?,
        val name: String?,
        @SerializedName("api_symbol")
        val apiSymbol: String?,
        val symbol: String?,
        @SerializedName("market_cap_rank")
        val marketCapRank: Int?,
        val thumb: String?,
        val large: String?
    )

    data class Exchange(
        val id: String?,
        val name: String?,
        @SerializedName("market_type")
        val marketType: String?,
        val thumb: String?,
        val large: String?
    )

    data class Category(
        val id: Int?,
        val name: String?
    )

    data class Nft(
        val id: String?,
        val name: String?,
        val symbol: String?,
        val thumb: String?
    )
}


fun CryptoSearchModelDto.Coin.toCryptoSearchModel(): CryptoSearchModel.Coin {
    return CryptoSearchModel.Coin(
        name = name,
        marketCapRank = marketCapRank,
        thumb = thumb
    )
}