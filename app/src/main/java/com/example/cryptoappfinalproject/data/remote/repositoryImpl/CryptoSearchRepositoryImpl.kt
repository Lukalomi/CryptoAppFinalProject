package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.dto.toCryptoSearchModel
import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedSearch
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.repository.CryptoSearchRepository
import javax.inject.Inject

class CryptoSearchRepositoryImpl @Inject constructor(
    private val fetchedSearch: FetchedSearch
) : CryptoSearchRepository {


    override suspend fun searchCoins(query: String): MutableList<CryptoSearchModel.Coin> {
        return fetchedSearch.searchCoins(query).coins!!.map { it.toCryptoSearchModel() }
            .toMutableList()

    }
}