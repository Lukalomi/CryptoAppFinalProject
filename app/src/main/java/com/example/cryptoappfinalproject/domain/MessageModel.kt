package com.example.cryptoappfinalproject.domain

data class MessageModel(
    var message: String? = null,
    var senderId: String? = null
) {
    constructor(): this("", "",)

}