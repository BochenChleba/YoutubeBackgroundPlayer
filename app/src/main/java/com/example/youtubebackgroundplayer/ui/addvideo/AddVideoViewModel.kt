package com.example.youtubebackgroundplayer.ui.addvideo

import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import com.example.youtubebackgroundplayer.util.parseVideoId

class AddVideoViewModel: BaseViewModel<AddVideoNavigator>() {

    fun parseInputToVideoId(input: String) {
        parseVideoId(input)
            ?.let { navigator.onVideoIdParsed(it) }
            ?: run { navigator.onInvalidInput() }
    }

    fun fetchVideoData(videoId: String) {
        //todo make api call
        val result = VideoDto(
            "tajtle",
            null,
            false,
            videoId
        )
        navigator.onVideoDataFetched(result)
    }
}
