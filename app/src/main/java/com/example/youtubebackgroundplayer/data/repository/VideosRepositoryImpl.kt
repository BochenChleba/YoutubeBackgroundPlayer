package com.example.youtubebackgroundplayer.data.repository

import com.example.youtubebackgroundplayer.data.dao.VideosDao
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.entity.VideoEntity
import com.example.youtubebackgroundplayer.util.Seconds

class VideosRepositoryImpl(
    private val videosDao: VideosDao
): VideosRepository {

    override suspend fun getVideosList(): List<VideoDto> =
        videosDao
            .getAll()
            .map { videoEntity ->
                VideoDto(
                    videoEntity.title,
                    videoEntity.duration?.let { Seconds(it) },
                    videoEntity.isWatched,
                    videoEntity.videoId
                )
            }

    override suspend fun insertVideos(videos: List<VideoDto>) {
        val entities = videos.map { dto ->
            VideoEntity(
                title = dto.title,
                duration = dto.duration?.seconds,
                isWatched = dto.isWatched,
                videoId = dto.videoId
            )
        }
        videosDao.insertAll(entities)
    }

}
