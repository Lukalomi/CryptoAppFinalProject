package com.example.cryptoappfinalproject.ui.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import javax.inject.Inject

class CoinsDataSource @Inject constructor(private val fetchedCrypto: FetchedCrypto) :
    PagingSource<Int, CryptoCoinsModel.CryptoCoinsModelItem>() {
    override fun getRefreshKey(state: PagingState<Int, CryptoCoinsModel.CryptoCoinsModelItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoCoinsModel.CryptoCoinsModelItem> {
        val page: Int = params.key ?: 1
        val prevPage = if (page != 1) page - 1 else null
        val nextPage = page + 1
        return try {
            val response = fetchedCrypto.getCoins(page)
            if (response.isSuccessful) {
                val coins = response.body() ?: mutableListOf()
                return LoadResult.Page(coins, prevPage, nextPage)
            } else {
                return LoadResult.Error(Throwable())
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}
