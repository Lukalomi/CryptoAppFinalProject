package com.example.cryptoappfinalproject.common

object CryptoEndPoints {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"

    const val getCoins = "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false"
    const val getExchanges = "exchanges"

    const val searchCoins = "search?query="
}

object ConversionEndPoint {
    const val BASE_URL = "https://api.exchangerate.host/"

    const val convertCoins = "convert"

}

object NewsEndPoint {
    const val BASE_URL_NEWS = "https://cryptonews-api.com/api/v1/"

    const val getCryptoNews = "category?section=general&items=3&page=1&token=aaeltrs4tbsptgb8i8kgxyqyuln2snrqrtue7wxr"
}

object VideosEndPoint {
    const val BASE_URL_YTVIDEO = "https://www.youtube.com/"

    const val getFirstVideo = "oembed?url=https://www.youtube.com/watch?v=SSo_EIwHSd4&t=10s&ab_channel=SimplyExplained&format=json"
    const val getSecondVideo = "oembed?url=https://www.youtube.com/watch?v=ZE2HxTmxfrI&ab_channel=SimplyExplained&format=json"
    const val getThirdVideo = "oembed?url=https://www.youtube.com/watch?v=M3EFi_POhps&ab_channel=SimplyExplainedformat=json"
}