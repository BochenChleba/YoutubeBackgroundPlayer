package com.example.youtubebackgroundplayer.data.dto

import com.example.youtubebackgroundplayer.data.model.GetPlaylistResponse
import com.example.youtubebackgroundplayer.data.model.GetVideoDetailsResponse
import com.example.youtubebackgroundplayer.util.time.Minutes
import com.example.youtubebackgroundplayer.util.time.Seconds

data class VideoDto(
    val title: String,
    val duration: Seconds? = null,
    val isWatched: Boolean = false,
    val videoId: String
) {
    companion object {
        @Throws(IllegalArgumentException::class)
        fun fromGetPlaylistResponse(response: GetPlaylistResponse): List<VideoDto> =
            response.items.map { responseItem ->
                VideoDto(
                    title = responseItem.snippet?.title
                        ?: throw IllegalArgumentException(),
                    videoId = responseItem.snippet.resourceId.videoId
                )
            }

        @Throws(IllegalArgumentException::class)
        fun fromGetVideoDetailsResponse(response: GetVideoDetailsResponse): VideoDto {
            val videoItem = response.items?.firstOrNull()
                ?: throw IllegalArgumentException()
            return VideoDto(
                title = videoItem.snippet?.title
                    ?: throw IllegalArgumentException(),
                duration = getDurationFromString(videoItem.contentDetails?.duration),
                videoId = videoItem.id
                    ?: throw IllegalArgumentException()
            )
        }

        private fun getDurationFromString(durationString: String?): Seconds? {
            durationString ?: return null
            val minutes = Minutes(
                findValuePrecedingChar(durationString, 'M')
            )
            val seconds = Seconds(
                findValuePrecedingChar(durationString, 'S')
            )
            return seconds + minutes
        }

        private fun findValuePrecedingChar(string: String, char: Char): Int {
            val index = string.indexOf(char)
            if (index != -1) {
                var outputString = ""
                for (i in index-1 downTo 0) {
                    val nextChar = string[i]
                    if (nextChar.isDigit()) {
                        outputString = nextChar + outputString
                    } else {
                        return outputString.toIntOrNull() ?: 0
                    }
                }
            }
            return 0
        }
    }
}
