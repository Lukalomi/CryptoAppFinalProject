package com.example.cryptoappfinalproject.common

object ApiEndPoints {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"

    const val getCoins = "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false"
    const val searchExchanges = "movie/popular?api_key=7ed909b5505ef24375a120e6e2c06512"
    const val searchCoins = "search?query="
}