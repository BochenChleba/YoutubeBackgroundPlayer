package com.example.youtubebackgroundplayer.ui.abstraction

import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.toast

abstract class BaseActivity<T: BaseViewModel<*>> : AppCompatActivity(), BaseNavigator {

    abstract val viewModel: T

    override fun showToast(text: String) {
        toast(text)
    }

    override fun showToast(resourceId: Int) {
        toast(getString(resourceId))
    }

}
