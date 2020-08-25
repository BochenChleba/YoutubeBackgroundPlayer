package com.example.youtubebackgroundplayer.ui.abstraction

import androidx.lifecycle.ViewModel
import com.example.youtubebackgroundplayer.data.sharedprefs.Preferences
import com.fasterxml.jackson.databind.ObjectMapper
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.ref.WeakReference

abstract class BaseViewModel<T: BaseNavigator>: ViewModel(), KoinComponent {
    val mapper: ObjectMapper by inject()
    val preferences: Preferences by inject()
    private lateinit var navigatorReference: WeakReference<T>

    protected val navigator: T
        get() = navigatorReference.get() ?: throw IllegalStateException()

    fun setNavigator(nav: T) {
        this.navigatorReference = WeakReference(nav)
    }
}
