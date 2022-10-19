package com.example.cryptoappfinalproject.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.domain.CryptoConverterModel
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(private val fetchedCrypto: FetchedCrypto) : ViewModel() {


    fun convertCrypto(
        id: String,
        currency: String
    ): Flow<Resource<MutableList<CryptoConverterModel.Bitcoin>>> =
        flow {
            val response = fetchedCrypto.convertCoins(id, currency)
            val value: Resource<MutableList<CryptoConverterModel.Bitcoin>>
            if (response.isSuccessful) {
                value = Resource.Success(response.body()?.bitcoin ?: mutableListOf())
            } else {
                value = Resource.Error(response.errorBody().toString() ?: "")
            }

            emit(value)
            emit(Resource.Loader(false))
        }
}

