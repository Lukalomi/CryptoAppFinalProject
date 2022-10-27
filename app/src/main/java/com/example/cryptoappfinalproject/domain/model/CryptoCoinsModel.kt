package com.example.cryptoappfinalproject.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

abstract class CryptoCoinsModel : MutableList<CryptoCoinsModel.CryptoCoinsModelItem> {
    data class CryptoCoinsModelItem(
        val name: String?,
        val image: String?,
        val currentPrice: Double?,
        val marketCapRank: Int?,
        val priceChangePercentage24h: Double?,
    )
}