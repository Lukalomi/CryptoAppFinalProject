package com.example.cryptoappfinalproject.domain

import androidx.annotation.DrawableRes

sealed interface ButtonTypes {
    data class Numeric(val number: Int): ButtonTypes
    data class Dot(val dot: String): ButtonTypes
    data class Remove(@DrawableRes val icon: Int, val action: ButtonActions): ButtonTypes
}

enum class ButtonActions {
    REMOVE
}