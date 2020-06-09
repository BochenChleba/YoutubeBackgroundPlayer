package com.example.youtubebackgroundplayer.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.database.AppDatabase
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
        //todo save, clear playlist recycler
        //todo set custom title on video data fetch failure
        //todo youtube API integration
        //todo settings
        //todo mute
        //todo changable order of recycler items
        //todo fullscreen mode
        //todo timer
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
