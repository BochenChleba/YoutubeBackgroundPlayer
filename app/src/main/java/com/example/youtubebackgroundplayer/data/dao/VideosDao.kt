package com.example.youtubebackgroundplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.youtubebackgroundplayer.constant.DatabaseConstants.TABLE_NAME_VIDEOS
import com.example.youtubebackgroundplayer.data.entity.VideoEntity

@Dao
interface VideosDao {
    @Query("SELECT COUNT(*) FROM videos")
    suspend fun count(): Int

    @Query("SELECT * FROM videos")
    suspend fun getAll(): List<VideoEntity>

    @Insert
    suspend fun insertAll(videos: List<VideoEntity>)

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getById(id: Int): VideoEntity

    @Query("DELETE FROM videos")
    suspend fun clear()
}
