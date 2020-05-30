package com.example.youtubebackgroundplayer.ui.playlist

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndOrderDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<PlaylistNavigator>() {

    private var cachedVideoList: MutableList<VideoDto>? = null

    fun loadVideoList() {
        viewModelScope.launch {
            val videosList = videosRepository.getVideosList()
/*            val xdList = mutableListOf<VideoDto>()
            for (i in 0..1000000) {
                xdList.addAll(videosList)
            }*/
            cachedVideoList = videosList.toMutableList()
            navigator.onVideoListLoaded(videosList)
        }
    }

    fun addVideoToCachedList(video: VideoDto) =
        cachedVideoList?.add(video)

    fun getVideoIdAndOrderByPosition(position: Int) =
        cachedVideoList
            ?.get(position)
            ?.toVideoIdAndOrderDto()
}
