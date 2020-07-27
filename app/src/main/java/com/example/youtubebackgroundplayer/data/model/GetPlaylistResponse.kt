package com.example.youtubebackgroundplayer.data.model

data class GetPlaylistResponse(
    val etag: String,
    val items: List<Item>,
    val kind: String
)