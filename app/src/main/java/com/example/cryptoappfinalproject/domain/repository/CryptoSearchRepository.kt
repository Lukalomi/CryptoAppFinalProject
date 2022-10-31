package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.data.remote.dto.CryptoSearchModelDto
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel

interface CryptoSearchRepository {

    suspend fun searchCoins (query: String) : MutableList<CryptoSearchModel.Coin>

}