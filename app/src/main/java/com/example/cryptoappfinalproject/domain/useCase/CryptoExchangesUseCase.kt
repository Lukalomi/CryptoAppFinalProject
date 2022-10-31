package com.example.cryptoappfinalproject.domain.useCase

import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.repository.CryptoExchangesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CryptoExchangesUseCase @Inject constructor(
    private val cryptoExchangesRepository: CryptoExchangesRepository
) {

    operator fun invoke()
    : Flow<Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>> = flow {
        try {
            emit(Resource.Loader(true))
            val getExchanges = cryptoExchangesRepository.searchExchanges()

            emit(Resource.Success(getExchanges))

        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage?: " An unexpected error occurred"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }

    }


}