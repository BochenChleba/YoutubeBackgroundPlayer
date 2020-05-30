package com.example.youtubebackgroundplayer.util

private const val YOUTUBE_VIDEO_ID_LENGTH = 11
private const val YOUTUBE_VIDEO_ID_PREFIX_1 = "youtu.be/"
private const val YOUTUBE_VIDEO_ID_PREFIX_2 = "watch?v="

fun parseVideoId(input: String): String? =
    when {
        input.length == YOUTUBE_VIDEO_ID_LENGTH -> {
            input
        }
        input.contains(YOUTUBE_VIDEO_ID_PREFIX_1) -> {
            getVideoIdFromInput(input, YOUTUBE_VIDEO_ID_PREFIX_1)
        }
        input.contains(YOUTUBE_VIDEO_ID_PREFIX_2) -> {
            getVideoIdFromInput(input, YOUTUBE_VIDEO_ID_PREFIX_2)
        }
        else -> {
            null
        }
    }

private fun getVideoIdFromInput(input: String, videoIdPrefix: String) =
    input.substringAfter(videoIdPrefix)
        .take(YOUTUBE_VIDEO_ID_LENGTH)
