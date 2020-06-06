package com.example.youtubebackgroundplayer.data.dto

import com.example.youtubebackgroundplayer.util.Seconds

class VideoDto(
    val title: String,
    val duration: Seconds?,
    val isWatched: Boolean,
    val videoId: String
)
