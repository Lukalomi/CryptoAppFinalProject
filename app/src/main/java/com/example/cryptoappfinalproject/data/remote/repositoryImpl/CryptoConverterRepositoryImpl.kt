package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.dto.toCryptoConverterModel
import com.example.cryptoappfinalproject.data.remote.FetchedConversion
import com.example.cryptoappfinalproject.domain.repository.CryptoConverterRepository
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel
import javax.inject.Inject

class CryptoConverterRepositoryImpl @Inject constructor (
    private val fetchedConversion : FetchedConversion
    ): CryptoConverterRepository {


    override suspend fun getConvertedCoins(from: String, to: String, amount: String): CryptoConverterModel {
        val response = fetchedConversion.convertCoins(from = from, to = to, amount = amount)

        if (response.isSuccessful) {
            return response.body()!!.toCryptoConverterModel()
        } else {
            return CryptoConverterModel("")
        }
    }


}