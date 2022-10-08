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
)