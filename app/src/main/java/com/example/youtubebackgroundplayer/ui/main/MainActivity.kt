package com.example.youtubebackgroundplayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.database.AppDatabase
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndOrderDto
import com.example.youtubebackgroundplayer.ui.player.PlayerFragment
import com.example.youtubebackgroundplayer.ui.playlist.PlaylistFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addVideoFromIntent(intent)
        //todo play next video
        //todo recycler item delete / clear all
        //todo set custom title on video data fetch failure
        //todo youtube API integration
        //todo settings
        //todo mute
        //todo changable order of recycler items
        //todo timer
        //todo fullscreen mode
    }

    private fun addVideoFromIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val videoId = intent.extras?.getString(Intent.EXTRA_TEXT)
            if (videoId != null) {
                showAddVideoDialogInPlaylistFragment(videoId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                GlobalScope.launch(Dispatchers.IO) {
                    get<AppDatabase>().clearAllTables()
                    withContext(Dispatchers.Main) {
                        toast("data cleared")
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is PlaylistFragment -> {
                fragment.onVideoSelected = { videoIdAndOrder ->
                    playVideoInPlayerFragment(videoIdAndOrder)
                }
            }
            is PlayerFragment -> {
                fragment.onNextVideoLoaded = { order ->
                    highlightNextVideoInPlaylistFragment(order)
                }
            }
        }
    }

    private fun playVideoInPlayerFragment(videoIdAndOrder: VideoIdAndOrderDto) {
        supportFragmentManager
            .findFragmentById(R.id.playerFragment)
            ?.let { it as PlayerFragment }
            ?.playVideo(videoIdAndOrder)
    }

    private fun highlightNextVideoInPlaylistFragment(videoOrder: Int) {
        supportFragmentManager
            .findFragmentById(R.id.playlistFragment)
            ?.let { it as PlaylistFragment }
            ?.selectItemInPlaylist(videoOrder)
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
