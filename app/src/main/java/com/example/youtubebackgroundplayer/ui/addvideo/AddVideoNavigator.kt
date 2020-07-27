package com.example.youtubebackgroundplayer.ui.addvideo

import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseNavigator

interface AddVideoNavigator: BaseNavigator {
    fun onInvalidInput()
    fun onInputParsed()
    fun onVideoDataFetched(videoDtoList: List<VideoDto>)
    fun onVideoDataFetchFailure(videoId: String)
    fun onPlaylistFetchFailure()
    fun onDataFetchFinished()
}
