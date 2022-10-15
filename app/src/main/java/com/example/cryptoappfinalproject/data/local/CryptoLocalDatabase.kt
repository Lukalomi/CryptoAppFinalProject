package com.example.cryptoappfinalproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cryptoappfinalproject.common.Converters
import com.google.android.gms.common.internal.safeparcel.SafeParcelable


@Database(entities = [Crypto::class,Exchanges::class, UserInfo::class], version = 10)
@TypeConverters(Converters::class)
abstract class CryptoLocalDatabase: RoomDatabase() {
    abstract fun CryptoDao():CryptoDao
}