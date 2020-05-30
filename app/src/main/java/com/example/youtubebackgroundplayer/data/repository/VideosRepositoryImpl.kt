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
                    videoEntity.videoId,
                    videoEntity.order
                )
            }

    override suspend fun insertVideo(video: VideoDto) {
        val order = videosDao.count()
        videosDao.insert(
            VideoEntity(
                title = video.title,
                duration = video.duration?.seconds,
                isWatched = video.isWatched,
                videoId = video.videoId,
                order = order
            )
        )
    }

    override suspend fun getVideoIdByOrder(order: Int) =
        videosDao.getVideoIdByOrder(order)
}
