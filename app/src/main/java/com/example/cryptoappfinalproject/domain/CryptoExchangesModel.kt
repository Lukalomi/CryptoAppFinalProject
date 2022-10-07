package com.example.cryptoappfinalproject.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class CryptoExchangesModel : ArrayList<CryptoExchangesModel.CryptoExchangesModelItem>(){
    @Parcelize
    data class CryptoExchangesModelItem(
        val id: String?,
        val name: String?,
        @SerializedName("year_established")
        val yearEstablished: Int?,
        val country: String?,
        val description: String?,
        val url: String?,
        val image: String?,
        @SerializedName("has_trading_incentive")
        val hasTradingIncentive: Boolean?,
        @SerializedName("trust_score")
        val trustScore: Int?,
        @SerializedName("trust_score_rank")
        val trustScoreRank: Int?,
        @SerializedName("trade_volume_24h_btc")
        val tradeVolume24hBtc: Double?,
        @SerializedName("trade_volume_24h_btc_normalized")
        val tradeVolume24hBtcNormalized: Double?
    ): Parcelable
}