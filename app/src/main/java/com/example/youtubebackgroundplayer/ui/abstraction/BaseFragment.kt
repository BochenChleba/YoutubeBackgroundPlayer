package com.example.youtubebackgroundplayer.ui.abstraction

import androidx.fragment.app.Fragment
import org.jetbrains.anko.toast

abstract class BaseFragment<T: BaseViewModel<*>> : Fragment(), BaseNavigator {

    abstract val viewModel: T

    override fun showToast(text: String) {
        context?.toast(text)
    }

    override fun showToast(resourceId: Int) {
        context?.toast(getString(resourceId))
    }
}
