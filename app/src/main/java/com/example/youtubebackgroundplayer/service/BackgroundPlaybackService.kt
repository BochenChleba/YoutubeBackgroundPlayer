package com.example.youtubebackgroundplayer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import androidx.core.app.NotificationCompat
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.constant.BundleConstants
import com.example.youtubebackgroundplayer.constant.Constants
import com.example.youtubebackgroundplayer.ext.player
import com.example.youtubebackgroundplayer.ui.main.MainActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.jetbrains.anko.toast


class BackgroundPlaybackService : Service() {
    private var currentVideoPosition = -1
    private var videosToPlay: Array<String> = emptyArray()
    private var timeElapsed = 0f
    private var isNextVideoLoading = false
    private var allVideosPlayed = false
    private lateinit var youtubeView: YouTubePlayerView

    override fun onBind(intent: Intent?): IBinder?
            = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runAsForeground()
        getExtras(intent)
        return START_STICKY
    }

    private fun runAsForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification: Notification =
            NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setContentText(applicationContext.getString(R.string.play_in_background_notification_text))
            .setContentIntent(pendingIntent)
            .build()
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification)
    }

    private fun getExtras(intent: Intent?) {
        videosToPlay = intent?.extras?.getStringArray(BundleConstants.BUNDLE_REMAINING_VIDEOS)
            ?: return
        val currentVideoSecond = intent.extras?.getFloat(BundleConstants.BUNDLE_TIME_ELAPSED)
            ?: 0f
        inflateYoutubePlayer(currentVideoSecond)
    }

    private fun inflateYoutubePlayer(timeElapsedInitialVideo: Float) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        youtubeView = inflater.inflate(R.layout.view_youtube_player, null) as YouTubePlayerView
        youtubeView.enableBackgroundPlayback(true)
        youtubeView.initialize(object : YouTubePlayerListener {
            override fun onReady(youTubePlayer: YouTubePlayer) {}
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                timeElapsed = second
            }
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {}
            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {}
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED -> {
                        if (!isNextVideoLoading) {
                            isNextVideoLoading = true
                            playNextVideo()
                        }
                    }
                    PlayerConstants.PlayerState.UNSTARTED -> {
                        isNextVideoLoading = false
                    }
                    else -> {}
                }
            }
            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
        })
        playNextVideo(timeElapsedInitialVideo)
    }

    private fun playNextVideo(timeElapsed: Float = 0f) {
        if (currentVideoPosition < videosToPlay.size - 1) {
            youtubeView.player {
                loadVideo(videosToPlay[++currentVideoPosition], timeElapsed)
            }
        } else {
            toast(R.string.player_no_next_video_toast)
            allVideosPlayed = true
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubeView.player {
            pause()
        }
        val intent = Intent(BundleConstants.BROADCAST_SERVICE_FINISHED)
            .apply {
                if (!allVideosPlayed) {
                    putExtra(BundleConstants.BUNDLE_VIDEO_ID, videosToPlay[currentVideoPosition])
                    putExtra(BundleConstants.BUNDLE_TIME_ELAPSED, timeElapsed)
                }
            }
        sendBroadcast(intent)
    }
}
