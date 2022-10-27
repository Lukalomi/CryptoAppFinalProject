package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel

interface CryptoExchangesRepository {

    suspend fun searchExchanges () : MutableList<CryptoExchangesModel.CryptoExchangesModelItem>

}