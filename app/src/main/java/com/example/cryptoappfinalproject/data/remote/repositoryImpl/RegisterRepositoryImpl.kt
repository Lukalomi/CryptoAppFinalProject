package com.example.cryptoappfinalproject.data.remote.repositoryImpl

//import com.example.cryptoappfinalproject.common.Resource
//import com.example.cryptoappfinalproject.data.remote.utils.await
//import com.example.cryptoappfinalproject.domain.repository.RegisterRepository
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.UserProfileChangeRequest
//import com.google.firebase.auth.ktx.userProfileChangeRequest
//import javax.inject.Inject
//
//class RegisterRepositoryImpl @Inject constructor(
//    private val firebaseAuth: FirebaseAuth
//): RegisterRepository {
//
//    override suspend fun register(email: String, password: String): Resource<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            Resource.Success(result.user!!)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Error(e)
//        }
//    }
//
//}