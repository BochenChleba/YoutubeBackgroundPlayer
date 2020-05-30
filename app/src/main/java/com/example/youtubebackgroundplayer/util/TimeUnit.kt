package com.example.youtubebackgroundplayer.util

import com.example.youtubebackgroundplayer.constant.DateTimeConstants
import com.example.youtubebackgroundplayer.constant.DateTimeConstants.MILLIS_IN_SECOND

interface TimeUnit {
    val millis: Long
}

inline class Seconds(val seconds: Long): TimeUnit {
    override val millis
        get() = seconds * MILLIS_IN_SECOND
}