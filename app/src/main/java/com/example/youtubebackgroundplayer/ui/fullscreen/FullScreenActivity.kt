package com.example.youtubebackgroundplayer.ui.fullscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.constant.BundleConstants.BUNDLE_CURRENT_VIDEO_SECOND
import com.example.youtubebackgroundplayer.constant.BundleConstants.BUNDLE_REMAINING_VIDEOS
import com.example.youtubebackgroundplayer.constant.BundleConstants.BUNDLE_VIDEO_ID
import com.example.youtubebackgroundplayer.ext.getFragment
import com.example.youtubebackgroundplayer.ext.player
import com.example.youtubebackgroundplayer.ui.player.PlayerFragment

class FullScreenActivity : AppCompatActivity() {

    private var currentVideoPosition = -1
    private var videosToPlay: Array<String> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goFullScreen()
        setContentView(R.layout.activity_fullscreen)

    }

    private fun goFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getBundleData()
    }

    private fun getBundleData() {
        videosToPlay = intent.extras?.getStringArray(BUNDLE_REMAINING_VIDEOS) ?: return
        val currentVideoSecond = intent.extras?.getFloat(BUNDLE_CURRENT_VIDEO_SECOND) ?: 0f
        if (videosToPlay.isNotEmpty()) {
            playNextVideoInFragment(currentVideoSecond)
        }
    }

    private fun playNextVideoInFragment(videoSecond: Float = 0f) =
        getFragment<PlayerFragment>(R.id.playerFragment)
            ?.playVideo(videosToPlay[++currentVideoPosition], videoSecond)

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is PlayerFragment -> {
                fragment.onVideoFinished = {
                    playNextVideoInFragment()
                }
                fragment.onFullscreenClicked = {
                    setResult()
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult()
        super.onBackPressed()
    }

    private fun setResult() {
        val currentSecond = getFragment<PlayerFragment>(R.id.playerFragment)?.currentSecond
        val currentVideoId = videosToPlay[currentVideoPosition]
        val intent = Intent().apply {
            putExtra(BUNDLE_CURRENT_VIDEO_SECOND, currentSecond)
            putExtra(BUNDLE_VIDEO_ID, currentVideoId)
        }
        setResult(Activity.RESULT_OK, intent)
    }

}
