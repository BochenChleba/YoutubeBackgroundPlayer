package com.example.youtubebackgroundplayer.ui.playlist

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndPositionDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<PlaylistNavigator>() {

    private var cachedVideosList: MutableList<VideoDto>? = null
    var currentVideoPosition: Int? = null

    fun loadVideoList() {
        viewModelScope.launch {
            if (cachedVideosList == null) {
                val videosList = videosRepository.getVideosList()
                cachedVideosList = videosList.toMutableList()
            }
            navigator.onVideoListLoaded(cachedVideosList ?: emptyList())
        }
    }

    fun addVideoToCachedList(videoDto: VideoDto) =
        cachedVideosList?.add(videoDto)

    fun removeVideoFromCachedList(position: Int) {
        cachedVideosList?.removeAt(position)
        if (currentVideoPosition!! >= position) {
            currentVideoPosition = currentVideoPosition?.minus(1)
        }
    }

    fun getNextVideoIdAndPosition(): VideoIdAndPositionDto {
        currentVideoPosition = currentVideoPosition?.plus(1)
        val nextVideoDto = currentVideoPosition?.let { currentPos ->
            if (currentPos < cachedVideosList?.size ?: 0) {
                cachedVideosList?.get(currentPos)
            } else {
                null
            }
        }
        return VideoIdAndPositionDto(nextVideoDto?.videoId, currentVideoPosition)
    }
}
