package com.example.youtubebackgroundplayer.data.dto

import com.example.youtubebackgroundplayer.util.Seconds

class VideoDto(
    val title: String,
    val duration: Seconds?,
    val isWatched: Boolean,
    val videoId: String,
    val order: Int
) {
    constructor(title: String, duration: Seconds?, isWatched: Boolean, videoId: String)
            : this(title, duration, isWatched, videoId, -1)
    fun toVideoIdAndOrderDto() =
        VideoIdAndOrderDto(videoId, order)
}


