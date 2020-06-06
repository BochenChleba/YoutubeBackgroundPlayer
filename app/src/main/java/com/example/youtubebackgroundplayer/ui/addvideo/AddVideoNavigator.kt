package com.example.youtubebackgroundplayer.ui.addvideo

import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseNavigator

interface AddVideoNavigator: BaseNavigator {
    fun onInvalidInput()
    fun onVideoIdParsed(videoId: String)
    fun onVideoDataFetched(videoDto: VideoDto)
    fun onVideoDataFetchFailure(videoId: String)
}
