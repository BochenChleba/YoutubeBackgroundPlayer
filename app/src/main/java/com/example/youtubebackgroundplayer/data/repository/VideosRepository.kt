package com.example.youtubebackgroundplayer.data.repository

import com.example.youtubebackgroundplayer.data.dto.VideoDto

interface VideosRepository {
    suspend fun getVideosList(): List<VideoDto>
    suspend fun setVideos(videos: List<VideoDto>)
    suspend fun clearAllVideos()
}