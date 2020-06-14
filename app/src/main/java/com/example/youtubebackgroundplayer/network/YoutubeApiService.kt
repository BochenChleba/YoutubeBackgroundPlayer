package com.example.youtubebackgroundplayer.network

import com.example.youtubebackgroundplayer.constant.NetworkConstants.FETCH_VIDEO_DETAILS_URL
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_API_KEY
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PARTS
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_PARTS_VAL
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_VIDEO_ID
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_API_KEY
import com.example.youtubebackgroundplayer.data.model.YoutubeApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET(FETCH_VIDEO_DETAILS_URL)
    suspend fun fetchVideoDetails(
        @Query(QUERY_VIDEO_ID) videoId: String,
        @Query(QUERY_PARTS) parts: String = QUERY_PARTS_VAL,
        @Query(QUERY_API_KEY) apiKey: String = YOUTUBE_API_KEY
    ): YoutubeApiResponse
}