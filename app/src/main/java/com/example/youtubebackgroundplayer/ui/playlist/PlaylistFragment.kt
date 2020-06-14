package com.example.youtubebackgroundplayer.ui.playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseFragment
import com.example.youtubebackgroundplayer.ui.addvideo.AddVideoDialog
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class PlaylistFragment: BaseFragment<PlaylistViewModel>(), KoinComponent, PlaylistNavigator {

    override val viewModel: PlaylistViewModel by viewModel()
    lateinit var onVideoSelected: (videoId: String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_playlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        context?.let { ctx ->
            setClickListeners(ctx)
            setRecycler(ctx)
        }
        viewModel.loadVideoList()
    }

    private fun setClickListeners(context: Context) {
        add_button.setOnClickListener {
            showAddVideoDialog()
        }
        clear_button.setOnClickListener {
            showClearPlaylistDialog(context)
        }
        save_button.setOnClickListener {
            viewModel.savePlaylist()
        }
    }

    private fun showAddVideoDialog() {
        AddVideoDialog.newInstance()
            .show(childFragmentManager, AddVideoDialog.TAG)
    }

    private fun showClearPlaylistDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.playlist_clear_dialog_title))
            .setMessage(getString(R.string.playlist_clear_dialog_content))
            .setPositiveButton(getString(R.string.playlist_clear_dialog_confirm)) { _, _ ->
                recyclerAdapter.clearItems()
                viewModel.clearPlaylist()
            }
            .setNegativeButton(getString(R.string.playlist_clear_dialog_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun setRecycler(context: Context) {
        videos_recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        videos_recycler_view.adapter = recyclerAdapter
    }

    private val recyclerAdapter = VideosRecyclerViewAdapter(
        onItemClick = { videoId, position ->
            viewModel.currentVideoPosition = position
            onVideoSelected(videoId)
        },
        onDeleteClick = { position ->
            viewModel.removeVideoFromCachedList(position)
        }
    )

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddVideoDialog) {
            childFragment.onVideoAdded = { videoDto ->
                storeNewVideo(videoDto)
            }
        }
    }

    private fun storeNewVideo(videoDto: VideoDto) {
        viewModel.addVideoToCachedList(videoDto)
        recyclerAdapter.addItem(videoDto)
        videos_recycler_view.smoothScrollToPosition(recyclerAdapter.itemCount - 1)
    }

    override fun onVideoListLoaded(videos: List<VideoDto>) {
        recyclerAdapter.setItems(videos)
    }

    fun showAddVideoFragment(videoId: String) {
        AddVideoDialog.newInstance(videoId)
            .show(childFragmentManager, AddVideoDialog.TAG)
    }

    fun playNextVideo() {
        val (nextVideoId, nextVideoPosition) = viewModel.getNextVideoIdAndPosition()
        if (nextVideoId != null && nextVideoPosition != null) {
            recyclerAdapter.select(nextVideoPosition)
            videos_recycler_view.smoothScrollToPosition(nextVideoPosition)
            onVideoSelected(nextVideoId)
        } else {
            toast(R.string.player_no_next_video_toast)
        }
    }
}
