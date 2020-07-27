package com.example.youtubebackgroundplayer.ui.settings

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.databinding.DialogSettingsBinding
import com.example.youtubebackgroundplayer.ext.addFadeAnimation
import com.example.youtubebackgroundplayer.ext.fadeWhenUnchecked
import com.example.youtubebackgroundplayer.ui.abstraction.BaseDialog
import kotlinx.android.synthetic.main.dialog_settings.*
import kotlinx.android.synthetic.main.layout_accept_cancel_buttons.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class SettingsDialog : BaseDialog(), KoinComponent, SettingsNavigator {
    companion object {
        const val TAG = "SettingsDialog"

        fun newInstance() =
            SettingsDialog().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? =
        DataBindingUtil.inflate<DialogSettingsBinding>(
            inflater,
            R.layout.dialog_settings,
            container,
            false
        )
            .also { binding -> binding.lifecycleOwner = this }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
        context ?: return
        loadSettings()
        setOnClickListeners()
    }

    private fun loadSettings() {
        disconnect_bluetooth_checkbox.apply {
            isChecked = viewModel.preferences.disconnectBluetooth
            fadeWhenUnchecked()
        }
        fullscreen_on_rotate_checkbox.apply {
            isChecked = viewModel.preferences.fullscreenOnRotate
            fadeWhenUnchecked()
        }
    }

    private fun setOnClickListeners() {
        accept_button.setOnClickListener {
            viewModel.preferences.disconnectBluetooth = disconnect_bluetooth_checkbox.isChecked
            viewModel.preferences.fullscreenOnRotate = fullscreen_on_rotate_checkbox.isChecked
            dismiss()
        }
        cancel_button.setOnClickListener {
            dismiss()
        }
        disconnect_bluetooth_checkbox.addFadeAnimation()
        fullscreen_on_rotate_checkbox.addFadeAnimation()
    }

}