package com.example.cryptoappfinalproject.presentation.ui.educational

import android.content.res.loader.ResourcesProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.model.VideoTitleModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EducationalViewModel @Inject constructor(
    private val resourcesProvider: com.example.cryptoappfinalproject.common.ResourcesProvider

) : ViewModel() {


    private var videosList: MutableList<VideoTitleModel> = mutableListOf()



    fun getVideos():MutableList<VideoTitleModel> {
        viewModelScope.launch {

           videosList.add(
                VideoTitleModel(
                    id = 1,
                    title = resourcesProvider.getString(R.string.video_one),
                    "https://i.ytimg.com/vi/SSo_EIwHSd4/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=SSo_EIwHSd4&t=1s&ab_channel=SimplyExplained"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 2,
                    title = resourcesProvider.getString(R.string.video_two),
                    "https://i.ytimg.com/vi/ZE2HxTmxfrI/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=ZE2HxTmxfrI&ab_channel=SimplyExplained"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 3,
                    title = resourcesProvider.getString(R.string.video_three),
                    "https://i.ytimg.com/vi/M3EFi_POhps/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=M3EFi_POhps&ab_channel=SimplyExplained"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 4,
                    title = resourcesProvider.getString(R.string.video_four),
                    "https://i.ytimg.com/vi/Pl8OlkkwRpc/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=Pl8OlkkwRpc&t=3s&ab_channel=TED"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 5,
                    title = resourcesProvider.getString(R.string.video_five),
                    "https://i.ytimg.com/vi/RplnSVTzvnU/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=RplnSVTzvnU&ab_channel=TED"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 6,
                    title = resourcesProvider.getString(R.string.video_six),
                    "https://i.ytimg.com/vi/97ufCT6lQcY/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=97ufCT6lQcY&ab_channel=TEDxTalks"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 7,
                    title = resourcesProvider.getString(R.string.video_sevem),
                    "https://i.ytimg.com/vi/PXSjbdOXgUE/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=PXSjbdOXgUE&ab_channel=99Bitcoins"
                )
            )
            videosList.add(
                VideoTitleModel(
                    id = 8,
                    title = resourcesProvider.getString(R.string.video_eight),
                    "https://i.ytimg.com/vi/3ZplgUKlDgI/hqdefault.jpg",
                    "https://www.youtube.com/watch?v=3ZplgUKlDgI&ab_channel=MoneyZG"
                )
            )
        }
return  videosList
    }

}