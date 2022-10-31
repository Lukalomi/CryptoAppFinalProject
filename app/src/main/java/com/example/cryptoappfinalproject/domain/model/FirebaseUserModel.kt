package com.example.cryptoappfinalproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseUserModel(
    val name:String,
    val email:String,
    val uid:String
):Parcelable{
    constructor(): this("", "","")
}

