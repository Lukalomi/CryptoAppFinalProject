package com.example.cryptoappfinalproject.ui.educational

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.remote.FetchVideoTitles
import com.example.cryptoappfinalproject.domain.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.VideoTitleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EducationalViewModel @Inject constructor(private val fetchVideoTitles:FetchVideoTitles) : ViewModel() {



    fun getYTTitle1(): Flow<VideoTitleModel> =
        flow {
            val response = fetchVideoTitles.getFirstVideoTitle()
            val value: VideoTitleModel
            if (response.isSuccessful) {
                value = response.body()!!
            } else {
                value = (response.errorBody() ?: "") as VideoTitleModel
            }

            emit(value)

        }

    fun getYTTitle2(): Flow<VideoTitleModel> =
        flow {
            val response = fetchVideoTitles.getSecondVideoTitle()
            val value: VideoTitleModel
            if (response.isSuccessful) {
                value = response.body()!!
            } else {
                value = (response.errorBody() ?: "") as VideoTitleModel
            }

            emit(value)

        }

    fun getYTTitles3(): Flow<VideoTitleModel> =
        flow {
            val response = fetchVideoTitles.getThirdVideoTitle()
            val value: VideoTitleModel
            if (response.isSuccessful) {
                value = response.body()!!
            } else {
                value = (response.errorBody() ?: "") as VideoTitleModel
            }

            emit(value)

        }


}