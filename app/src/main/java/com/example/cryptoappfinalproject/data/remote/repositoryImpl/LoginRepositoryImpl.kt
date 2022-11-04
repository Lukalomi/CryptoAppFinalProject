package com.example.cryptoappfinalproject.data.remote.repositoryImpl

//import com.example.cryptoappfinalproject.common.Resource
//import com.example.cryptoappfinalproject.domain.repository.LoginRepository
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.coroutines.tasks.await
//import javax.inject.Inject
//
//class LoginRepositoryImpl @Inject constructor(
//    private val firebaseAuth: FirebaseAuth
//) : LoginRepository {
//
//    override val currentUser: FirebaseUser?
//        get() = firebaseAuth.currentUser
//
//
//    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//            Resource.Success(result.user!!)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Resource.Error(e)
//        }
//    }
//
//}