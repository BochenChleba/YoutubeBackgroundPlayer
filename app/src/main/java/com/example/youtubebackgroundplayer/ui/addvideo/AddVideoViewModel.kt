package com.example.youtubebackgroundplayer.ui.addvideo

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.network.YoutubeApiService
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import com.example.youtubebackgroundplayer.util.videoid.parseVideoId
import kotlinx.coroutines.launch

class AddVideoViewModel(
    private val youtubeApiService: YoutubeApiService
): BaseViewModel<AddVideoNavigator>() {

    fun parseInputToVideoId(input: String) {
        parseVideoId(input)
            ?.let { navigator.onVideoIdParsed(it) }
            ?: run { navigator.onInvalidInput() }
    }

    fun fetchVideoData(videoId: String) {
        viewModelScope.launch {
            try {
                val response = youtubeApiService.fetchVideoDetails(videoId)
                val videoDto = VideoDto.fromYoutubeApiResponse(response)
                navigator.onVideoDataFetched(videoDto)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                navigator.onVideoDataFetchFailure(videoId)
            }
        }
    }
}
