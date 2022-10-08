package com.example.cryptoappfinalproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.cryptoappfinalproject.data.remote.FetchedCrypto
import com.example.cryptoappfinalproject.ui.datasource.CoinsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val fetchedCrypto: FetchedCrypto) : ViewModel() {

    val coinsPager =
        Pager(config = PagingConfig(30), pagingSourceFactory = { CoinsDataSource(fetchedCrypto) }).flow.cachedIn(
            viewModelScope
        )
}