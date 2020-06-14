package com.example.youtubebackgroundplayer.data.repository

import com.example.youtubebackgroundplayer.data.dao.VideosDao
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.entity.VideoEntity
import com.example.youtubebackgroundplayer.util.time.Seconds

class VideosRepositoryImpl(
    private val videosDao: VideosDao
): VideosRepository {

    override suspend fun getVideosList(): List<VideoDto> =
        videosDao
            .getAll()
            .map { videoEntity ->
                VideoDto(
                    videoEntity.title,
                    videoEntity.duration?.let {
                        Seconds(
                            it
                        )
                    },
                    videoEntity.isWatched,
                    videoEntity.videoId
                )
            }

    override suspend fun setVideos(videos: List<VideoDto>) =
        videos
            .map { dto ->
                VideoEntity(
                    title = dto.title,
                    duration = dto.duration?.seconds,
                    isWatched = dto.isWatched,
                    videoId = dto.videoId
                )
            }
            .let { entities ->
                videosDao.clear()
                videosDao.insertAll(entities)
            }

    override suspend fun clearAllVideos() =
        videosDao.clear()

}
