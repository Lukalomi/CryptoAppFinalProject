package com.example.cryptoappfinalproject.presentation.ui.converter

import androidx.lifecycle.ViewModel
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoConverterRepositoryImpl
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(private val FetchedConversion: CryptoConverterRepositoryImpl) : ViewModel() {


    fun convertCrypto(from: String, to: String, amount: String):
            Flow<Resource<CryptoConverterModel>> =

        flow {
            val response = FetchedConversion.getConvertedCoins ( from, to, amount)
            emit(Resource.Success(response))
            emit(Resource.Loader(false))
        }
}

