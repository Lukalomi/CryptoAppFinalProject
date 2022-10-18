package com.example.cryptoappfinalproject.common

object ApiEndPoints {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"

    const val getCoins = "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false"
    const val searchExchanges = "exchanges"
    const val searchCoins = "search?query="
    const val convertCoins = "simple/price"


    const val BASE_URL_NEWS = "https://cryptonews-api.com/api/v1/"

    const val getCryptoNews = "category?section=general&items=50&page=1&token=bbzwkoiuenp9x8d42ldtr7vmz0n3kc5d8drvbibb"
}