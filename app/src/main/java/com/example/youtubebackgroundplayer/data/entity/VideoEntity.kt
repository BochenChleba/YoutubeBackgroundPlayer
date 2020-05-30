package com.example.youtubebackgroundplayer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="videos")
data class VideoEntity (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var duration: Long?,
    var isWatched: Boolean,
    var videoId: String,
    var order: Int
) {
    constructor(title: String, duration: Long?, isWatched: Boolean, videoId: String, order: Int)
            : this(0, title, duration, isWatched, videoId, order)
}
