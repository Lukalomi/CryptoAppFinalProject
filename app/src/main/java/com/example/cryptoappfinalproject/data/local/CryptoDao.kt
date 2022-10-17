package com.example.cryptoappfinalproject.data.local

import androidx.room.*

@Dao
interface CryptoDao {
    @Query("SELECT * FROM crypto")
    suspend fun getAll(): MutableList<Crypto>

    @Query("DELETE FROM crypto")
    suspend fun deleteAll()

    @Insert
    suspend fun insertAll(vararg cryptos: Crypto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCrypto(movie: Crypto)

    @Delete
    suspend fun delete(crypto: Crypto)



    @Query("SELECT * FROM exchange")
    suspend fun getAllExchanges(): MutableList<Exchanges>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExchange(exchange: Exchanges)

    @Delete
    suspend fun deleteExchange(exchange: Exchanges)



    @Query("SELECT * FROM userInfo")
    suspend fun getAllUserInfo(): MutableList<UserInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserInfo(user: UserInfo)

    @Update
    suspend fun updateUserInfo(user: UserInfo)

    @Delete
    suspend fun deleteUserInfo(user: UserInfo)

    @Query("DELETE FROM userInfo")
    suspend fun deleteAllUsers()

}