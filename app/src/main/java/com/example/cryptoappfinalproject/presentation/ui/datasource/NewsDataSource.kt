package com.example.cryptoappfinalproject.presentation.ui.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoappfinalproject.data.remote.FetchedNews
import com.example.cryptoappfinalproject.data.remote.dto.CryptoNewsModelDto
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoNewsRepositoryImpl
import com.example.cryptoappfinalproject.domain.model.CryptoNewsModel
import javax.inject.Inject

class NewsDataSource @Inject constructor(private val fetchedNews: CryptoNewsRepositoryImpl) :
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
                return LoadResult.Page(response, prevPage, nextPage)

        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}