package com.example.cryptoappfinalproject.domain

data class CryptoConverterModel(
    val bitcoin: MutableList<Bitcoin>?
) {
    data class Bitcoin(
        val usd: Double?
    )
}