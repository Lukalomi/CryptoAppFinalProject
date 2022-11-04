package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.common.Resource
import com.google.firebase.auth.FirebaseUser

interface RegisterRepository {
    suspend fun register (email: String, password: String) : Resource<FirebaseUser>
}