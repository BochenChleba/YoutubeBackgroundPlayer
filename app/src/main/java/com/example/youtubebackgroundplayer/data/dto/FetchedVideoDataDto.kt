package com.example.youtubebackgroundplayer.data.dto

import com.example.youtubebackgroundplayer.util.Seconds

class FetchedVideoDataDto(
    val videoId: String,
    val title: String,
    val duration: Seconds?
)
