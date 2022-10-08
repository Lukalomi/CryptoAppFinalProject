package com.example.cryptoappfinalproject.data.local

import androidx.room.*

@Dao
interface CryptoDao {
    @Query("SELECT * FROM crypto")
    suspend fun getAll(): List<Crypto>

    @Query("DELETE FROM crypto")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg cryptos: Crypto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCrypto(movie: Crypto)

    @Delete
    suspend fun delete(crypto: Crypto)

}