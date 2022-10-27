package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoExchangeModel
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.repository.CryptoExchangesRepository
import javax.inject.Inject

class CryptoExchangesRepositoryImpl @Inject constructor(
    private val fetchedCrypto: FetchedCrypto

) : CryptoExchangesRepository {
    override suspend fun searchExchanges(): MutableList<CryptoExchangesModel.CryptoExchangesModelItem> {
        val response = fetchedCrypto.searchExchanges()

        if (response.isSuccessful){
            return response.body()!!.map { it.toCryptoExchangeModel() }.toMutableList()
        } else {
            return emptyList<CryptoExchangesModel.CryptoExchangesModelItem>().toMutableList()
        }
    }
}