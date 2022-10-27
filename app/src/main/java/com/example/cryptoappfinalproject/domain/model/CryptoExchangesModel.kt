package com.example.cryptoappfinalproject.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

abstract class CryptoExchangesModel : MutableList<CryptoExchangesModel.CryptoExchangesModelItem> {
    data class CryptoExchangesModelItem(
        val name: String?,
        val image: String?,
        val trustScoreRank: Int?,
        val tradeVolume24hBtc: Double?,
        val tradeVolume24hBtcNormalized: Double?
    )
}