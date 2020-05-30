package com.example.youtubebackgroundplayer.ui.abstraction

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.jetbrains.anko.toast

abstract class BaseDialog : DialogFragment(), BaseNavigator {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun showToast(text: String) {
        context?.toast(text)
    }

    override fun showToast(resourceId: Int) {
        context?.toast(getString(resourceId))
    }
}
