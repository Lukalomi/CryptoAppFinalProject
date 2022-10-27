package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel

interface CryptoConverterRepository {

    suspend fun getConvertedCoins (from: String, to: String, amount: String)
    : CryptoConverterModel

}