package com.example.cryptoappfinalproject.common

sealed class Resource<Any> {
    data class Success<Any>(val data: Any): Resource<Any>()
    data class Error<T:Any>(val errorMsg: String) : Resource<T>()
    data class Loader<T:Any>(val isLoading: Boolean): Resource<T>()
}