package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoCoinsModel
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.repository.CryptoCoinsRepository
import javax.inject.Inject

class CryptoCoinsRepositoryImpl @Inject constructor(
    private val fetchedCrypto: FetchedCrypto
) : CryptoCoinsRepository {

    override suspend fun getCoins(page: Int): MutableList<CryptoCoinsModel.CryptoCoinsModelItem> {
        val response = fetchedCrypto.getCoins(page = page)

        if (response.isSuccessful) {
            return response.body()!!.map { it.toCryptoCoinsModel() }.toMutableList()
        } else {
            return emptyList<CryptoCoinsModel.CryptoCoinsModelItem>().toMutableList()
        }

    }
}