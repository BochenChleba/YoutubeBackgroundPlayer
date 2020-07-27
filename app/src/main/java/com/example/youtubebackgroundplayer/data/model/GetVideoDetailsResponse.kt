package com.example.youtubebackgroundplayer.data.model

data class GetVideoDetailsResponse(
    val etag: String?,
    val items: List<Item>?,
    val kind: String?
)
