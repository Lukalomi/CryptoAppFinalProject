package com.example.cryptoappfinalproject.domain.model

data class MessageModel(
    var message: String? = null,
    var senderId: String? = null
) {
    constructor(): this("", "",)

}