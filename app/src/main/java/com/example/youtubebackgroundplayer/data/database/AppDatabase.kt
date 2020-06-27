package com.example.youtubebackgroundplayer.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.youtubebackgroundplayer.constant.DataConstants
import com.example.youtubebackgroundplayer.data.dao.VideosDao
import com.example.youtubebackgroundplayer.data.entity.VideoEntity

@Database(entities = [VideoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object{}
    abstract fun videosDao(): VideosDao
}

fun AppDatabase.Companion.provideInstance(application: Application) =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        DataConstants.DATABASE_NAME
    ).build()

