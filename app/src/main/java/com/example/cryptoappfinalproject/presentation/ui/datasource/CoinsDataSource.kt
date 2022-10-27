package com.example.cryptoappfinalproject.presentation.ui.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.dto.CryptoCoinsModelDto
import com.example.cryptoappfinalproject.data.remote.repositoryImpl.CryptoCoinsRepositoryImpl
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.repository.CryptoCoinsRepository
import javax.inject.Inject

class CoinsDataSource @Inject constructor(private val fetchedCrypto: CryptoCoinsRepositoryImpl) :
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
            return LoadResult.Page(response, prevPage, nextPage)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}
