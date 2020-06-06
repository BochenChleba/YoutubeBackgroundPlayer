package com.example.youtubebackgroundplayer.ui.playlist

import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseNavigator

interface PlaylistNavigator: BaseNavigator {
    fun onVideoListLoaded(videos: List<VideoDto>)
    fun onVideoRemoved(position: Int)
}
