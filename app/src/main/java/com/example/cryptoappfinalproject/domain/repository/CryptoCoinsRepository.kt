package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel

interface CryptoCoinsRepository  {
    suspend fun getCoins (page: Int): MutableList<CryptoCoinsModel.CryptoCoinsModelItem>

}