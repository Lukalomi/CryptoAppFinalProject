package com.example.cryptoappfinalproject.presentation.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.CryptoDao
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.domain.repository.RegisterRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val cryptoDao: CryptoDao,
//    private val registerRepository: RegisterRepository
    ) : ViewModel() {

//    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow
//
//
//    fun register(email: String, password: String) = viewModelScope.launch {
//        _registerFlow.value = Resource.Loader(true)
//        val result = registerRepository.register(email, password)
//        _registerFlow.value = result
//    }

    fun readAllUserInfo() = flow {
        emit(cryptoDao.getAllUserInfo())
    }



    fun updateUserInfo(user: UserInfo) {
        viewModelScope.launch {
            cryptoDao.updateUserInfo(user)
        }
    }

    fun insertUserInfo(user: UserInfo) {
        viewModelScope.launch {
            cryptoDao.addUserInfo(user)
        }
    }



}