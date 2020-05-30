package com.example.youtubebackgroundplayer.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class VideoBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        2+2
        Toast.makeText(context, "gowno", Toast.LENGTH_SHORT).show()
    }
}
