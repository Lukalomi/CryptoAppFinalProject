package com.example.cryptoappfinalproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cryptoappfinalproject.common.BitmapConverter



@Database(entities = [Crypto::class,Exchanges::class, UserInfo::class], version = 14)
@TypeConverters(BitmapConverter::class)
abstract class CryptoLocalDatabase: RoomDatabase() {
    abstract fun CryptoDao():CryptoDao
}