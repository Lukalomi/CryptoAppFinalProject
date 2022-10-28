package com.example.cryptoappfinalproject.data.local

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userInfo")
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "image")
    val image: Bitmap,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "surname")
    val surname: String,
    @ColumnInfo(name = "email")
    var email: String?,
    @ColumnInfo(name = "password")
    var password: String,
)
