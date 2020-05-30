package com.example.youtubebackgroundplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.youtubebackgroundplayer.data.dao.VideosDao
import com.example.youtubebackgroundplayer.data.entity.VideoEntity

@Database(entities = [VideoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videosDao(): VideosDao
}
