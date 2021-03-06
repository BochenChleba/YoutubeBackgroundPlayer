package com.example.youtubebackgroundplayer.ui.main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.youtubebackgroundplayer.service.BackgroundPlaybackService
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.constant.BundleConstants
import com.example.youtubebackgroundplayer.constant.BundleConstants.BUNDLE_REMAINING_VIDEOS
import com.example.youtubebackgroundplayer.constant.BundleConstants.BUNDLE_TIME_ELAPSED
import com.example.youtubebackgroundplayer.constant.BundleConstants.REQUEST_CODE_FULLSCREEN
import com.example.youtubebackgroundplayer.constant.Constants
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndTimeElapsedDto
import com.example.youtubebackgroundplayer.ext.getFragment
import com.example.youtubebackgroundplayer.ui.fullscreen.FullScreenActivity
import com.example.youtubebackgroundplayer.ui.player.PlayerFragment
import com.example.youtubebackgroundplayer.ui.playlist.PlaylistFragment
import com.example.youtubebackgroundplayer.ui.settings.SettingsDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var soundEnabled = true
    private val audioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val backgroundPlaybackBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val videoId = intent?.getStringExtra(BundleConstants.BUNDLE_VIDEO_ID)
                ?: return
            val elapsedTime = intent.getFloatExtra(BundleConstants.BUNDLE_TIME_ELAPSED, 0f)
            getFragment<PlaylistFragment>(R.id.playlistFragment)
                ?.selectVideoByVideoId(VideoIdAndTimeElapsedDto(videoId, elapsedTime))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerBroadcastReceiver()
        stopBackgroundPlaybackService()
        requestBatteryOptimizationDisable()
        createNotificationChannel()
        addVideoFromIntent(intent)
        setSoundEnabledState()
        setPlayInBackgroundButton()
    }

    private fun registerBroadcastReceiver() {
        registerReceiver(
            backgroundPlaybackBroadcastReceiver,
            IntentFilter(BundleConstants.BROADCAST_SERVICE_FINISHED)
        )
    }

    private fun stopBackgroundPlaybackService() {
        stopService(Intent(this, BackgroundPlaybackService::class.java))
    }

    @SuppressLint("BatteryLife")
    private fun requestBatteryOptimizationDisable() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:$packageName")
            }
            startActivity(intent)
        }
    }

    private fun addVideoFromIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val videoId = intent.extras?.getString(Intent.EXTRA_TEXT)
            if (videoId != null) {
                getFragment<PlaylistFragment>(R.id.playlistFragment)
                    ?.showAddVideoFragment(videoId)
            }
        }
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        )
    }

    private fun setSoundEnabledState() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        soundEnabled = currentVolume != 0
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(backgroundPlaybackBroadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return false
        menuInflater.inflate(R.menu.menu_main, menu)
        setSoundMenuItemDrawable(menu)
        return true
    }

    private fun setSoundMenuItemDrawable(menu: Menu) {
        menu.findItem(R.id.action_mute).icon =
            getDrawable(
                if (soundEnabled) {
                    R.drawable.ic_volume_on
                } else {
                    R.drawable.ic_volume_off
                }
            )
    }

    private fun setPlayInBackgroundButton() {
        backgroundPlaybackButton.setOnClickListener {
            val remainingVideos = getFragment<PlaylistFragment>(R.id.playlistFragment)
                ?.getRemainingVideos()
                ?.toTypedArray()
            val currentSecond = getFragment<PlayerFragment>(R.id.playerFragment)
                ?.currentSecond
            if (remainingVideos != null && currentSecond != null && remainingVideos.isNotEmpty()) {
                val intent = Intent(this, BackgroundPlaybackService::class.java)
                    .apply {
                        putExtra(BUNDLE_REMAINING_VIDEOS, remainingVideos)
                        putExtra(BUNDLE_TIME_ELAPSED, currentSecond)
                    }
                startForegroundService(intent)
                finish()
            } else {
                toast(R.string.play_in_background_no_videos_toast)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_settings -> {
                showSettingsDialog()
                true
            }
            R.id.action_mute -> {
                toggleSoundEnabled()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    private fun showSettingsDialog() =
        SettingsDialog.newInstance().show(supportFragmentManager, SettingsDialog.TAG)

    private fun toggleSoundEnabled() {
        soundEnabled = !soundEnabled
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_TOGGLE_MUTE,0)
        invalidateOptionsMenu()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is PlaylistFragment -> {
                fragment.onVideoSelected = { videoId, videoSeconds ->
                    getFragment<PlayerFragment>(R.id.playerFragment)
                        ?.playVideo(videoId, videoSeconds)
                }
            }
            is PlayerFragment -> {
                fragment.onVideoFinished = {
                    getFragment<PlaylistFragment>(R.id.playlistFragment)
                        ?.playNextVideo()
                }
                fragment.onFullscreenClicked = { currentVideoSecond ->
                    showFullscreenActivity(currentVideoSecond)
                }
            }
        }
    }

    private fun showFullscreenActivity(currentVideoSecond: Float) {
        val remainingVideos = getFragment<PlaylistFragment>(R.id.playlistFragment)
            ?.getRemainingVideos()
            ?.toTypedArray()
        startActivityForResult<FullScreenActivity>(
            REQUEST_CODE_FULLSCREEN,
            BUNDLE_TIME_ELAPSED to currentVideoSecond,
            BUNDLE_REMAINING_VIDEOS to remainingVideos
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        addVideoFromIntent(intent)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getFragment<PlayerFragment>(R.id.playerFragment)
            ?.showPrompt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == REQUEST_CODE_FULLSCREEN) {
            val videoId = data.getStringExtra(BundleConstants.BUNDLE_VIDEO_ID) ?: return
            val timeElapsed = data.getFloatExtra(BundleConstants.BUNDLE_TIME_ELAPSED, 0f)
            getFragment<PlaylistFragment>(R.id.playlistFragment)
                ?.selectVideoByVideoId(VideoIdAndTimeElapsedDto(videoId, timeElapsed))
        }
    }
}
