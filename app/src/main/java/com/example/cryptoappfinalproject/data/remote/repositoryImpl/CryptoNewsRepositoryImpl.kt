package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.FetchedNews
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoNewsModel
import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel
import com.example.cryptoappfinalproject.domain.repository.CryptoNewsRepository
import javax.inject.Inject

class CryptoNewsRepositoryImpl @Inject constructor(
    private val fetchedNews: FetchedNews
): CryptoNewsRepository {
    override suspend fun getNews(page: Int): MutableList<CryptoNewsModel.Data> {
        val response = fetchedNews.getNews(page)

        if (response.isSuccessful){
            return response.body()!!.map { it.toCryptoNewsModel() }.toMutableList()
        } else {
            return emptyList<CryptoNewsModel.Data>().toMutableList()
        }

    }
}