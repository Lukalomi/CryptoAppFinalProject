package com.example.cryptoappfinalproject.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import com.example.cryptoappfinalproject.ui.datasource.CoinsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val fetchedCrypto: FetchedCrypto) : ViewModel() {


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
            val response = fetchedCrypto.searchCoins(query)
            if (response.isSuccessful) {
                _newState.value = Resource.Success(response.body()?.coins ?: mutableListOf())
            } else {
                val errorBody = response.errorBody()
                _newState.value = Resource.Error(errorBody?.toString() ?: "")
            }
        }
    }


    fun getExchanges(): Flow<Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>> =
        flow {
            val response = fetchedCrypto.searchExchanges()
            val value: Resource<MutableList<CryptoExchangesModel.CryptoExchangesModelItem>>
            if (response.isSuccessful) {
                value = Resource.Success(response.body() ?: mutableListOf())
            } else {
                value = Resource.Error(response.errorBody().toString() ?: "")
            }

            emit(value)
            emit(Resource.Loader(false))

        }
}