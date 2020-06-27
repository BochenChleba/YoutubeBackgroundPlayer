package com.example.youtubebackgroundplayer.data.sharedprefs

import android.content.SharedPreferences

private const val KEY_DISCONNECT_BLUETOOTH = "DISCONNECT_BLUETOOTH"
private const val KEY_FULLSCREEN_ON_ROTATE = "FULLSCREEN_ON_ROTATE"

interface Preferences {
    val sharedPreferences: SharedPreferences

    var disconnectBluetooth: Boolean
        get() =
            sharedPreferences.getBoolean(KEY_DISCONNECT_BLUETOOTH, false)
        set(value) {
            sharedPreferences.edit().also { editor ->
                editor.putBoolean(KEY_DISCONNECT_BLUETOOTH, value)
                editor.apply()
            }
        }

    var fullscreenOnRotate: Boolean
        get() =
            sharedPreferences.getBoolean(KEY_FULLSCREEN_ON_ROTATE, false)
        set(value) {
            sharedPreferences.edit().also { editor ->
                editor.putBoolean(KEY_FULLSCREEN_ON_ROTATE, value)
                editor.apply()
            }
        }
}