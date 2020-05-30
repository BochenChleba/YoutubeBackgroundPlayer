package com.example.youtubebackgroundplayer.ui.player

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndOrderDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<PlayerNavigator>() {

    fun loadNextVideo(currentVideoOrder: Int) {
        viewModelScope.launch {
            val nextVideoOrder = currentVideoOrder + 1
            val nextVideoId = videosRepository.getVideoIdByOrder(nextVideoOrder)
            if (nextVideoId != null) {
                navigator.onNextVideoIdLoaded(
                    VideoIdAndOrderDto(nextVideoId, nextVideoOrder)
                )
            } else {
                navigator.showToast(R.string.player_no_next_video_toast)
            }
        }
    }
}
