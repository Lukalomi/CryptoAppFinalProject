package com.example.cryptoappfinalproject.domain.useCase

import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.dto.CryptoConverterModelDto
import com.example.cryptoappfinalproject.data.remote.dto.toCryptoConverterModel
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel
import com.example.cryptoappfinalproject.domain.repository.CryptoConverterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConvertCryptoUseCase @Inject constructor(
    private val cryptoConverterRepository: CryptoConverterRepository
) {

    operator fun invoke (from: String, to: String, amount: String) :
            Flow<Resource<CryptoConverterModel>> = flow {

        try {
            emit(Resource.Loader(true))
            val convert = cryptoConverterRepository.getConvertedCoins(from, to, amount).body()!!
                .toCryptoConverterModel()

            emit(Resource.Success(convert))

        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage?: " An unexpected error occurred"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }

    }

}