package com.example.cryptoappfinalproject.domain.useCase

import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.repository.CryptoSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val cryptoSearchRepository: CryptoSearchRepository
) {
    operator fun invoke (query: String): Flow<Resource<MutableList<CryptoSearchModel.Coin>>>
        = flow {
        try {
            emit(Resource.Loader(true))
            val search = cryptoSearchRepository.searchCoins(query)

            emit(Resource.Success(search))

        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage?: " An unexpected error occurred"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }

    }

}