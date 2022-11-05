package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.data.remote.dto.CryptoConverterModelDto
import retrofit2.Response

interface CryptoConverterRepository {

    suspend fun getConvertedCoins (from: String, to: String, amount: String)
    : Response<CryptoConverterModelDto>

}