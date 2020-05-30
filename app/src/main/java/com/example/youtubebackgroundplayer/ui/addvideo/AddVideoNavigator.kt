package com.example.youtubebackgroundplayer.ui.addvideo

import com.example.youtubebackgroundplayer.data.dto.FetchedVideoDataDto
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseNavigator

interface AddVideoNavigator: BaseNavigator {
    fun onInvalidInput()
    fun onVideoIdParsed(videoId: String)
    fun onVideoDataFetched(fetchedVideoDataDto: FetchedVideoDataDto)
    fun onVideoDataFetchFailure(videoId: String)
    fun onVideoSaved(videoDto: VideoDto)
}
