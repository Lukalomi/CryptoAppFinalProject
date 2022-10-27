package com.example.cryptoappfinalproject.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoCoinsRepositoryImpl
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoExchangesRepositoryImpl
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoSearchRepositoryImpl
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.presentation.ui.datasource.CoinsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchedCrypto: CryptoCoinsRepositoryImpl,
    private val searchCrypto: CryptoSearchRepositoryImpl,
    private val exchangeCrypto: CryptoExchangesRepositoryImpl,
    ) : ViewModel() {


    val coinsPager =
            Pager(
                config = PagingConfig(30),
                pagingSourceFactory = { CoinsDataSource(fetchedCrypto) }).flow.cachedIn(
                viewModelScope
            )




    private val _newState =
        MutableStateFlow<Resource<MutableList<CryptoSearchModel.Coin>>>(
            Resource.Success(
                mutableListOf()
            )
        )
    val newState = _newState.asStateFlow()


    fun getSearchedCryptos(query: String) {
        viewModelScope.launch {
            val response = searchCrypto.searchCoins(query)

                _newState.value = Resource.Success(response)

                _newState.value = Resource.Error( "Error")
            }
        }



    fun getExchanges(): Flow<Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>> =
        flow {
            val response = exchangeCrypto.searchExchanges()
            emit(Resource.Success(response))
            emit(Resource.Loader(false))


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
}