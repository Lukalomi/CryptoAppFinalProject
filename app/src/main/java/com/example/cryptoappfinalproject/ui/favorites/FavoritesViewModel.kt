package com.example.cryptoappfinalproject.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.CryptoDao
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val cryptoDao: CryptoDao) : ViewModel() {


    fun readAllData() = flow {
        emit(cryptoDao.getAll())
    }



    fun insertCrypto(crypto: Crypto) {
        viewModelScope.launch {
            cryptoDao.addCrypto(crypto)
        }
    }

    suspend fun deleteAllData() = cryptoDao.deleteAll()


    fun deleteCrypto(crypto: Crypto) {
        viewModelScope.launch {
            cryptoDao.delete(crypto)
        }
    }
}