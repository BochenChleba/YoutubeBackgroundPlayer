package com.example.youtubebackgroundplayer.ui.abstraction

import androidx.lifecycle.ViewModel
import com.example.youtubebackgroundplayer.data.sharedprefs.Preferences
import com.fasterxml.jackson.databind.ObjectMapper
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel<T: BaseNavigator>: ViewModel(), KoinComponent {
    lateinit var navigator: T
    val mapper: ObjectMapper by inject()
    val prefs: Preferences by inject()
}