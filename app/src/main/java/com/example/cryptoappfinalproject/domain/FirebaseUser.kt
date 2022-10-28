package com.example.cryptoappfinalproject.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseUser(
    val name:String,
    val email:String,
    val uid:String
):Parcelable{
    constructor(): this("", "","")
}

