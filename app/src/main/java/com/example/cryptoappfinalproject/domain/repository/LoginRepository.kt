package com.example.cryptoappfinalproject.domain.repository

import com.example.cryptoappfinalproject.common.Resource
import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
        val currentUser: FirebaseUser?
        suspend fun login(email: String, password: String): Resource<FirebaseUser>

}