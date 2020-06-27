package com.example.youtubebackgroundplayer.di

import com.example.youtubebackgroundplayer.data.database.AppDatabase
import com.example.youtubebackgroundplayer.data.database.provideInstance
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.data.repository.VideosRepositoryImpl
import com.example.youtubebackgroundplayer.data.sharedprefs.Preferences
import com.example.youtubebackgroundplayer.data.sharedprefs.PreferencesImpl
import com.example.youtubebackgroundplayer.network.RetrofitProvider
import com.example.youtubebackgroundplayer.network.YoutubeApiService
import com.example.youtubebackgroundplayer.network.YoutubeApiServiceImpl
import com.example.youtubebackgroundplayer.ui.addvideo.AddVideoViewModel
import com.example.youtubebackgroundplayer.ui.player.PlayerViewModel
import com.example.youtubebackgroundplayer.ui.playlist.PlaylistViewModel
import com.example.youtubebackgroundplayer.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PlaylistViewModel(get()) }
    viewModel { AddVideoViewModel(get()) }
    viewModel { PlayerViewModel() }
    viewModel { SettingsViewModel() }
}

val dataModule = module {
    single<Preferences> { PreferencesImpl(androidContext()) }
    single<VideosRepository> { VideosRepositoryImpl(get()) }
    single { AppDatabase.provideInstance(androidApplication() ) }
    single { get<AppDatabase>().videosDao() }
}

val networkModule = module {
    single { RetrofitProvider.provide() }
    single<YoutubeApiService> { YoutubeApiServiceImpl(get()) }
}
