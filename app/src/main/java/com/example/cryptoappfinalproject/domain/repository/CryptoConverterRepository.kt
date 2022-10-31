package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.data.remote.dto.CryptoConverterModelDto
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel
import retrofit2.Response

interface CryptoConverterRepository {

    suspend fun getConvertedCoins (from: String, to: String, amount: String)
    : Response<CryptoConverterModelDto>

}