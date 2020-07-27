package com.example.youtubebackgroundplayer.network

import com.example.youtubebackgroundplayer.constant.NetworkConstants.FETCH_PLAYLIST_URL
import com.example.youtubebackgroundplayer.constant.NetworkConstants.FETCH_VIDEO_DETAILS_URL
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_API_KEY
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_MAX_RESULTS
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_MAX_RESULTS_VAL
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PARTS
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PARTS_SNIPPET
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PARTS_SNIPPET_CONTENT_DETAILS
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PLAYLIST_ID
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_VIDEO_ID
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_API_KEY
import com.example.youtubebackgroundplayer.data.model.GetPlaylistResponse
import com.example.youtubebackgroundplayer.data.model.GetVideoDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET(FETCH_VIDEO_DETAILS_URL)
    suspend fun fetchVideoDetails(
        @Query(QUERY_VIDEO_ID) videoId: String,
        @Query(QUERY_PARTS) parts: String = QUERY_PARTS_SNIPPET_CONTENT_DETAILS,
        @Query(QUERY_API_KEY) apiKey: String = YOUTUBE_API_KEY
    ): GetVideoDetailsResponse

    @GET(FETCH_PLAYLIST_URL)
    suspend fun fetchPlaylist(
        @Query(QUERY_PLAYLIST_ID) playlistId: String,
        @Query(QUERY_MAX_RESULTS) maxResults: String = QUERY_MAX_RESULTS_VAL,
        @Query(QUERY_PARTS) parts: String = QUERY_PARTS_SNIPPET,
        @Query(QUERY_API_KEY) apiKey: String = YOUTUBE_API_KEY
    ): GetPlaylistResponse
}