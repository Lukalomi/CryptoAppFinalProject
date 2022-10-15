package com.example.cryptoappfinalproject.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exchange")
data class Exchanges(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "rank")
    val rank: Int,
    @ColumnInfo(name = "current_Price")
    val volume: Double?,
)
