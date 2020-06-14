package com.example.youtubebackgroundplayer.util.time

interface MeasureOfTime {
    val millis: Long
    val displayableTime: String
    operator fun plus(another: MeasureOfTime): MeasureOfTime
}
