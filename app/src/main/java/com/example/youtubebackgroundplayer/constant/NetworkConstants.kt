package com.example.youtubebackgroundplayer.constant

object NetworkConstants {
    const val YOUTUBE_API_KEY = "PUT_API_KEY"
    const val BASE_URL = "https://www.googleapis.com"
    const val QUERY_SEPARATOR = '&'
    const val QUERY_PARTS = "part"
    const val QUERY_PARTS_SNIPPET_CONTENT_DETAILS = "snippet,contentDetails"
    const val QUERY_PARTS_SNIPPET = "snippet"
    const val QUERY_VIDEO_ID = "id"
    const val QUERY_API_KEY = "key"
    const val QUERY_PLAYLIST_ID = "playlistId"
    const val QUERY_MAX_RESULTS = "maxResults"
    const val QUERY_MAX_RESULTS_VAL = "50"
    const val FETCH_VIDEO_DETAILS_URL = "/youtube/v3/videos"
    const val FETCH_PLAYLIST_URL = "/youtube/v3/playlistItems"
    const val YOUTUBE_VIDEO_ID_LENGTH = 11
    const val YOUTUBE_VIDEO_ID_PREFIX_1 = "youtu.be/"
    const val YOUTUBE_VIDEO_ID_PREFIX_2 = "watch?v="
    const val YOUTUBE_PLAYLIST_PREFIX = "playlist?list="
}