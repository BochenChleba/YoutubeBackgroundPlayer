package com.example.youtubebackgroundplayer.ui.player

import com.example.youtubebackgroundplayer.data.dto.VideoIdAndOrderDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseNavigator

interface PlayerNavigator: BaseNavigator {
    fun onNextVideoIdLoaded(videoIdAndOrder: VideoIdAndOrderDto)
}
