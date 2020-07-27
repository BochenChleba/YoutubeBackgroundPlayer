package com.example.youtubebackgroundplayer.network

import retrofit2.Retrofit

class YoutubeApiServiceImpl(retrofit: Retrofit): YoutubeApiService {
    private val apiService = retrofit.create(YoutubeApiService::class.java)

    override suspend fun fetchVideoDetails(videoId: String, parts: String, apiKey: String) =
        apiService.fetchVideoDetails(videoId, parts, apiKey)

    override suspend fun fetchPlaylist(playlistId: String, maxResults: String, parts: String, apiKey: String) =
        apiService.fetchPlaylist(playlistId, maxResults, parts, apiKey)
}
