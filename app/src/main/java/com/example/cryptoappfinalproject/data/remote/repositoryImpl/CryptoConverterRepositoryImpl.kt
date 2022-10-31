package com.example.cryptoappfinalproject.data.remote.repositoryImpl

import com.example.cryptoappfinalproject.data.remote.fetchApi.FetchedConvert
import com.example.cryptoappfinalproject.data.remote.dto.CryptoConverterModelDto
import com.example.cryptoappfinalproject.domain.repository.CryptoConverterRepository
import retrofit2.Response
import javax.inject.Inject

class CryptoConverterRepositoryImpl @Inject constructor (
    private val fetchedConversion : FetchedConvert
    ): CryptoConverterRepository {


    override suspend fun getConvertedCoins(from: String, to: String, amount: String):
            Response<CryptoConverterModelDto> {

        return fetchedConversion.convertCoins(from, to, amount)

    }

}