package com.example.youtubebackgroundplayer.ui.playlist

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<PlaylistNavigator>() {

    fun loadVideoList() {
        viewModelScope.launch {
            val videosList = videosRepository.getVideosList()
            navigator.onVideoListLoaded(videosList)
        }
    }
}
