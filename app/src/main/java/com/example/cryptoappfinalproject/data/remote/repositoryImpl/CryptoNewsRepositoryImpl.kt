package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedNews
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoNewsModel
import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel
import com.example.cryptoappfinalproject.domain.repository.CryptoNewsRepository
import javax.inject.Inject

class CryptoNewsRepositoryImpl @Inject constructor(
    private val fetchedNews: FetchedNews
): CryptoNewsRepository {
    override suspend fun getNews(page: Int): MutableList<CryptoNewsModel.Data> {
        return fetchedNews.getNews(page).data!!.map { it.toCryptoNewsModel() }.toMutableList()

    }
}