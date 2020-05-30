package com.example.youtubebackgroundplayer.ui

import android.app.Application
import com.example.youtubebackgroundplayer.di.dataModule
import com.example.youtubebackgroundplayer.di.networkModule
import com.example.youtubebackgroundplayer.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@MyApplication)
            modules(viewModelModule, dataModule, networkModule)
        }
    }
}
