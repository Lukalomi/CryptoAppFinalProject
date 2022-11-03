package com.example.cryptoappfinalproject.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoCoinsRepositoryImpl
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.useCase.CryptoExchangesUseCase
import com.example.cryptoappfinalproject.domain.useCase.SearchUseCase
import com.example.cryptoappfinalproject.presentation.ui.datasource.CoinsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchedCrypto: CryptoCoinsRepositoryImpl,
    private val searchUseCase: SearchUseCase,
    private val cryptoExchangesUseCase: CryptoExchangesUseCase
    ) : ViewModel() {


    val coinsPager =
            Pager(
                config = PagingConfig(30),
                pagingSourceFactory = { CoinsDataSource(fetchedCrypto) }).flow.cachedIn(
                viewModelScope
            )


    private val _searchState =
        MutableStateFlow<Resource<MutableList<CryptoSearchModel.Coin>>>(
            Resource.Success(mutableListOf())
        )
    val searchState = _searchState.asStateFlow()

    fun getSearchedCryptos(query: String) {

        _searchState.value = Resource.Success(mutableListOf())

        searchUseCase(query).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _searchState.value = Resource.Success(result.data)
                }
                is Resource.Error -> {
                    _searchState.value = Resource.Error(result.errorMsg)
                }
                is Resource.Loader -> {
                    _searchState.value = Resource.Loader(result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }


    //
//    fun getSearchedCryptos(query: String) {
//        viewModelScope.launch {
//            val response = searchCrypto.searchCoins(query)
//
//                _newState.value = Resource.Success(response)
//
//                _newState.value = Resource.Error( "Error")
//            }
//        }



    private val _exchangesState =
        MutableStateFlow<Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>>(Resource.Loader(false))
    val exchangesState = _exchangesState.asStateFlow()

    fun getExchanges() {
        cryptoExchangesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _exchangesState.value = Resource.Success(result.data)
                }
                is Resource.Error -> {
                    _exchangesState.value = Resource.Error(result.errorMsg)
                }
                is Resource.Loader -> {
                    _exchangesState.value = Resource.Loader(result.isLoading)
                }
            }
        }.launchIn(viewModelScope)
    }

//            val value: Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>
//            if (response.isSuccessful) {
//                value = Resource.Success(response.body() ?: mutableListOf())
//            } else {
//                value = Resource.Error(response.errorBody().toString() ?: "")
//            }
//
//            emit(value)
//            emit(Resource.Loader(false))


}