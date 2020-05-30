package com.example.youtubebackgroundplayer.ui.addvideo

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.data.dto.FetchedVideoDataDto
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import com.example.youtubebackgroundplayer.util.parseVideoId
import kotlinx.coroutines.launch

class AddVideoViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<AddVideoNavigator>() {

    fun parseInputToVideoId(input: String) {
        parseVideoId(input)
            ?.let { navigator.onVideoIdParsed(it) }
            ?: run { navigator.onInvalidInput() }
    }

    fun fetchVideoData(videoId: String) {
        //todo make api call
        val result = FetchedVideoDataDto(
            videoId,
            "tajtle",
            null
        )
        navigator.onVideoDataFetched(result)
    }

    fun saveVideo(dto: FetchedVideoDataDto) {
        viewModelScope.launch {
            val videoDto = VideoDto(
                dto.title,
                dto.duration,
                false,
                dto.videoId
            )
            videosRepository.insertVideo(videoDto)
            navigator.onVideoSaved(videoDto)
        }
    }
}
