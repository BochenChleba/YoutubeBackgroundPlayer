package com.example.youtubebackgroundplayer.di

import androidx.room.Room
import com.example.youtubebackgroundplayer.constant.DatabaseConstants
import com.example.youtubebackgroundplayer.data.database.AppDatabase
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.data.repository.VideosRepositoryImpl
import com.example.youtubebackgroundplayer.data.sharedprefs.Preferences
import com.example.youtubebackgroundplayer.network.RetrofitProvider
import com.example.youtubebackgroundplayer.network.YoutubeApiService
import com.example.youtubebackgroundplayer.network.YoutubeApiServiceImpl
import com.example.youtubebackgroundplayer.ui.addvideo.AddVideoViewModel
import com.example.youtubebackgroundplayer.ui.player.PlayerViewModel
import com.example.youtubebackgroundplayer.ui.playlist.PlaylistViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModelModule = module {
    viewModel { PlaylistViewModel(get()) }
    viewModel { AddVideoViewModel(get()) }
    viewModel { PlayerViewModel() }
}

val dataModule = module {
    single { Preferences(androidContext()) }
    single<VideosRepository> { VideosRepositoryImpl(get()) }
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()
    }
    single { get<AppDatabase>().videosDao() }
}

val networkModule = module {
    single { RetrofitProvider.provide() }
    single<YoutubeApiService> { YoutubeApiServiceImpl(get()) }
}
