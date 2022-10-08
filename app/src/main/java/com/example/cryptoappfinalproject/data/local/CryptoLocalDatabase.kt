package com.example.cryptoappfinalproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Crypto::class], version = 1)
abstract class CryptoLocalDatabase: RoomDatabase() {
    abstract fun CryptoDao():CryptoDao
}