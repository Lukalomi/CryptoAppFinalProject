package com.example.cryptoappfinalproject.data.remote.dto

import com.example.cryptoappfinalproject.domain.model.CryptoConverterModel

class CryptoConverterModelDto (
    val result: String?
)

fun CryptoConverterModelDto.toCryptoConverterModel(): CryptoConverterModel {
    return CryptoConverterModel(
        result = result
    )
}