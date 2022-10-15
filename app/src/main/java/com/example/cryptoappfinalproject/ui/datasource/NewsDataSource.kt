package com.example.cryptoappfinalproject.ui.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.FetchedNews
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoNewsModel
import javax.inject.Inject

class NewsDataSource @Inject constructor(private val fetchedNews: FetchedNews) :
    PagingSource<Int, CryptoNewsModel.Data>() {
    override fun getRefreshKey(state: PagingState<Int, CryptoNewsModel.Data>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CryptoNewsModel.Data> {
        val page: Int = params.key ?: 1
        val prevPage = if (page != 1) page - 1 else null
        val nextPage = page + 1
        return try {
            val response = fetchedNews.getNews(page)
            if (response.isSuccessful) {
                val news = response.body()?.data ?: emptyList()
                return LoadResult.Page(news, prevPage, nextPage)
            } else {
                return LoadResult.Error(Throwable())
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}