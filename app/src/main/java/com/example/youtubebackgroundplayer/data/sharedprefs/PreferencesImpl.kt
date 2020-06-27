package com.example.youtubebackgroundplayer.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences

private const val NAME = "PC_REMOTE_PREFS"

class PreferencesImpl(context: Context) : Preferences {
    override val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
}