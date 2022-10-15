package com.example.cryptoappfinalproject.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.data.local.CryptoDao
import com.example.cryptoappfinalproject.data.local.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(private val cryptoDao: CryptoDao) : ViewModel() {

    fun readAllUserInfo() = flow {
        emit(cryptoDao.getAllUserInfo())
    }

    fun deleteUserInfo(user: UserInfo) {
        viewModelScope.launch {
            cryptoDao.deleteUserInfo(user)
        }
    }

    fun insertUserInfo(user: UserInfo) {
        viewModelScope.launch {
            cryptoDao.addUserInfo(user)
        }
    }



}