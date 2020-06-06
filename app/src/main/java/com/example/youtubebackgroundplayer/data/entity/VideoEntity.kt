package com.example.youtubebackgroundplayer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.util.Seconds
import com.example.youtubebackgroundplayer.util.TimeUnit

@Entity(tableName="videos")
data class VideoEntity (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var duration: Long?,
    var isWatched: Boolean,
    var videoId: String
) {
    constructor(title: String, duration: Long?, isWatched: Boolean, videoId: String)
            : this(0, title, duration, isWatched, videoId)

    fun toVideoDto() =
        VideoDto(
            title,
            duration?.let { Seconds(it) },
            isWatched,
            videoId
        )
}
