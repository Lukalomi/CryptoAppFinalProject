package com.example.cryptoappfinalproject.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class LoginViewModel @Inject constructor(
//    private val loginRepository: LoginRepository
//) : ViewModel() {
//
//    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
//    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow
//
//    val currentUser: FirebaseUser?
//    get() = loginRepository.currentUser
//
//
//    init {
//        if (loginRepository.currentUser != null) {
//            _loginFlow.value = Resource.Success(loginRepository.currentUser!!)
//        }
//    }
//
//    fun login(email: String, password: String) = viewModelScope.launch {
//        _loginFlow.value = Resource.Loader(true)
//        val result = loginRepository.login(email, password)
//        _loginFlow.value = result
//    }
//
//}