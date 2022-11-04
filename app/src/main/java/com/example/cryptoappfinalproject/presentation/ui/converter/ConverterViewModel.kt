package com.example.cryptoappfinalproject.presentation.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel
import com.example.cryptoappfinalproject.domain.useCase.ConvertCryptoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val convertCryptoUseCase: ConvertCryptoUseCase
    ) : ViewModel() {

    private val _state = MutableStateFlow<Resource<CryptoConverterModel>>(Resource.Loader(false))
    val state = _state.asStateFlow()


    fun convertCrypto(from: String, to: String, amount: String) {
        convertCryptoUseCase(from, to, amount).onEach { result ->
            when (result) {
                is Resource.Success -> {
                   _state.value = Resource.Success(result.data)

                }

                is Resource.Error -> {
                    _state.value = Resource.Error(result.errorMsg)

                }

                is Resource.Loader -> {
                    _state.value = Resource.Loader(result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }
}

