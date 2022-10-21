package com.example.cryptoappfinalproject.domain


data class CryptoConverterModel(
    val bitcoin: Bitcoin?
) {
    data class Bitcoin(
        val usd: Double?
    )
}