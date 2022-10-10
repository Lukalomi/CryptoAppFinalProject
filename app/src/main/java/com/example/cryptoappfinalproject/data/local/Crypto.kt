package com.example.cryptoappfinalproject.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto")
data class Crypto(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "original_title")
    val originalTitle: String,
    @ColumnInfo(name = "marketcap_Rank")
    val marketCapRank: Int,
    @ColumnInfo(name = "current_Price")
    val currentPrice: Double?,
    @ColumnInfo(name = "price_change")
    val priceChangePercentage24h: Double?
)