package com.example.cryptoappfinalproject.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.data.remote.FetchedNews
import com.example.cryptoappfinalproject.ui.datasource.CoinsDataSource
import com.example.cryptoappfinalproject.ui.datasource.NewsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(private val fetchedNews: FetchedNews) : ViewModel() {


    val newsPager =
        Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { NewsDataSource(fetchedNews) }).flow.cachedIn(
            viewModelScope
        )

}