package com.example.cryptoappfinalproject.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.domain.CryptoConverterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
//
//@HiltViewModel
//class ConverterViewModel @Inject constructor(private val fetchedCrypto: FetchedCrypto) : ViewModel() {
//
//    private val _newState =
//        MutableStateFlow<Resource<MutableList<CryptoConverterModel>>>(
//            Resource.Success(
//                mutableListOf()
//            )
//        )
//    val newState = _newState.asStateFlow()
//
//
//    fun convertCryptos(query: String) {
//        viewModelScope.launch {
//            val response = fetchedCrypto.convertCoins(query, query)
//            if (response.isSuccessful) {
//                _newState.value = Resource.Success(response.body()?.coins ?: mutableListOf())
//            } else {
//                val errorBody = response.errorBody()
//                _newState.value = Resource.Error(errorBody?.toString() ?: "")
//            }
//        }
//    }
//










