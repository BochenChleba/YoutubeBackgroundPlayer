package com.example.youtubebackgroundplayer.ext

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

fun YouTubePlayerView.player(block: YouTubePlayer.() -> Unit) {
    getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
        override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
            block(youTubePlayer)
        }
    })
}
