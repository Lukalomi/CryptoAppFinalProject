package com.example.cryptoappfinalproject.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.CryptoDao
import com.example.cryptoappfinalproject.data.local.Exchanges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val cryptoDao: CryptoDao) : ViewModel() {


    fun readAllData() = flow {
        emit(cryptoDao.getAll())
    }

    fun readAllExchanges() = flow {
        emit(cryptoDao.getAllExchanges())
    }

    fun deleteExchange(exchanges: Exchanges) {
        viewModelScope.launch {
            cryptoDao.deleteExchange(exchanges)
        }
    }

    fun insertExchange(exchanges: Exchanges) {
        viewModelScope.launch {
            cryptoDao.addExchange(exchanges)
        }
    }

    fun insertCrypto(crypto: Crypto) {
        viewModelScope.launch {
            cryptoDao.addCrypto(crypto)
        }
    }



    fun deleteCrypto(crypto: Crypto) {
        viewModelScope.launch {
            cryptoDao.delete(crypto)
        }
    }
}