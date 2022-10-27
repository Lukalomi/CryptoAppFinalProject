package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.data.remote.dto.CryptoCoinsModelDto
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel

interface CryptoCoinsRepository  {
    suspend fun getCoins (page: Int): MutableList<CryptoCoinsModel.CryptoCoinsModelItem>

}