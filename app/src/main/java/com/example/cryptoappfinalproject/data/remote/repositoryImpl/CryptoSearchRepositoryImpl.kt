package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoSearchModel
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.repository.CryptoSearchRepository
import javax.inject.Inject

class CryptoSearchRepositoryImpl @Inject constructor(
    private val fetchedCrypto: FetchedCrypto
) : CryptoSearchRepository {
    override suspend fun searchCoins(query: String): MutableList<CryptoSearchModel.Coin> {
        val response = fetchedCrypto.searchCoins(query = query)

        if (response.isSuccessful) {
            return response.body()!!.map { it.toCryptoSearchModel() }.toMutableList()
        } else {
            return emptyList<CryptoSearchModel.Coin>().toMutableList()
        }
    }

}