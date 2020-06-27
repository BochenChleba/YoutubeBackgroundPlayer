package com.example.youtubebackgroundplayer.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.ui.player.PlayerFragment
import com.example.youtubebackgroundplayer.ui.playlist.PlaylistFragment
import com.example.youtubebackgroundplayer.ui.settings.SettingsDialog


class MainActivity : AppCompatActivity() {

    private val audioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var soundEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestBatteryOptimizationDisable()
        addVideoFromIntent(intent)
        setSoundEnabledState()
        //todo fullscreen mode
    }

    @SuppressLint("BatteryLife")
    private fun requestBatteryOptimizationDisable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent().apply {
                    action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }
        }
    }

    private fun addVideoFromIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val videoId = intent.extras?.getString(Intent.EXTRA_TEXT)
            if (videoId != null) {
                showAddVideoDialogInPlaylistFragment(videoId)
            }
        }
    }

    private fun setSoundEnabledState() {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        soundEnabled = currentVolume != 0
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
                fragment.onVideoSelected = { videoId ->
                    playVideoInPlayerFragment(videoId)
                }
            }
            is PlayerFragment -> {
                fragment.onVideoFinished = {
                    playNextVideoFromPlaylist()
                }
            }
        }
    }

    private fun playVideoInPlayerFragment(videoId: String) {
        supportFragmentManager
            .findFragmentById(R.id.playerFragment)
            ?.let { it as PlayerFragment }
            ?.playVideo(videoId)
    }

    private fun playNextVideoFromPlaylist() {
        supportFragmentManager
            .findFragmentById(R.id.playlistFragment)
            ?.let { it as PlaylistFragment }
            ?.playNextVideo()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        addVideoFromIntent(intent)
    }

    private fun showAddVideoDialogInPlaylistFragment(videoUrl: String) {
        supportFragmentManager
            .findFragmentById(R.id.playlistFragment)
            ?.let { it as PlaylistFragment }
            ?.showAddVideoFragment(videoUrl)
    }
}
