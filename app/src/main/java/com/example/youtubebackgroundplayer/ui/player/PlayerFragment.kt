package com.example.youtubebackgroundplayer.ui.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.ext.player
import com.example.youtubebackgroundplayer.ui.abstraction.BaseFragment
import com.example.youtubebackgroundplayer.ui.fullscreen.FullScreenActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.view_youtube_player.*
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.anko.support.v4.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : BaseFragment<PlayerViewModel>(), PlayerNavigator {

    override val viewModel: PlayerViewModel by viewModel()
    lateinit var onVideoFinished: () -> Unit
    lateinit var onFullscreenClicked: (currentVideoSecond: Float) -> Unit
    private var isNextVideoLoading = false
    var currentSecond = 0f
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_player, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
        context?.let { ctx ->
            setPlayer(ctx)
        }
    }

    private fun setPlayer(context: Context) {
        val fullscreenImageView = ImageView(context).apply {
            setImageDrawable(context.getDrawable(R.drawable.ic_fullscreen))
            setOnClickListener {
                youtube_player_view.player {
                    pause()
                }
                onFullscreenClicked(currentSecond)
            }
        }
        youtube_player_view.getPlayerUiController().addView(fullscreenImageView)
        youtube_player_view.initialize(object :
            YouTubePlayerListener {
            override fun onReady(youTubePlayer: YouTubePlayer) {}
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentSecond = second
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
                            onVideoFinished()
                        }
                    }
                    PlayerConstants.PlayerState.UNSTARTED -> {
                        isNextVideoLoading = false
                    }
                    else -> {}
                }
            }
            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                hidePrompt()
            }
            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
        })
    }

    fun showPrompt() {
        youtube_player_view.visibility = View.INVISIBLE
        promptTextView.visibility = View.VISIBLE
    }

    private fun hidePrompt() {
        youtube_player_view.visibility = View.VISIBLE
        promptTextView.visibility = View.INVISIBLE
    }

    fun playVideo(videoId: String, startSeconds: Float = 0f) {
        youtube_player_view.player {
            loadVideo(videoId, startSeconds)
        }
    }

    fun seekTo(seconds: Float) {
        youtube_player_view.player {
            seekTo(seconds)
        }
    }
}
