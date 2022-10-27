package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel

interface CryptoNewsRepository {

    suspend fun getNews(page: Int) : MutableList<CryptoNewsModel.Data>

}