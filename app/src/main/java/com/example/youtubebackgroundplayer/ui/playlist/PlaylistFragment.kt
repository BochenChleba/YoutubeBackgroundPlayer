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
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndTimeElapsedDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseFragment
import com.example.youtubebackgroundplayer.ui.addvideo.AddVideoDialog
import com.example.youtubebackgroundplayer.util.rv.ItemMoveCallbackListener
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class PlaylistFragment: BaseFragment<PlaylistViewModel>(), KoinComponent, PlaylistNavigator {

    override val viewModel: PlaylistViewModel by viewModel()
    lateinit var onVideoSelected: (videoId: String, videoSeconds: Float) -> Unit
    lateinit var recyclerAdapter: VideosRecyclerViewAdapter
    lateinit var touchHelper: ItemTouchHelper
    private var cachedVideoIdAndTimeElapsedDto: VideoIdAndTimeElapsedDto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_playlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
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
    }

    private fun showAddVideoDialog() {
        AddVideoDialog.newInstance()
            .show(childFragmentManager, AddVideoDialog.TAG)
    }

    private fun showClearPlaylistDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.playlist_clear_dialog_title))
            .setMessage(getString(R.string.playlist_clear_dialog_content))
            .setPositiveButton(getString(R.string.clear)) { _, _ ->
                recyclerAdapter.clearItems()
                viewModel.clearPlaylist()
            }
            .setNegativeButton(getString(R.string.dismiss)) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun setRecycler(context: Context) {
        videos_recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        recyclerAdapter = VideosRecyclerViewAdapter(
            onItemClick = { videoId, position ->
                viewModel.currentVideoPosition = position
                onVideoSelected(videoId, 0f)
            },
            onDeleteClick = { position ->
                viewModel.removeVideoFromCachedList(position)
            },
            onStartDrag = { holder ->
                touchHelper.startDrag(holder)
            },
            onFinishDrag = { items, position ->
                viewModel.updatePlaylistState(items, position)
            }
        )
        val callback = ItemMoveCallbackListener(recyclerAdapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(videos_recycler_view)
        videos_recycler_view.adapter = recyclerAdapter
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddVideoDialog) {
            childFragment.onVideosAdded = { videoDtoList ->
                storeNewVideo(videoDtoList)
            }
        }
    }

    private fun storeNewVideo(videoDtoList: List<VideoDto>) {
        viewModel.addVideosToCachedList(videoDtoList)
        recyclerAdapter.addItems(videoDtoList)
        videos_recycler_view.smoothScrollToPosition(recyclerAdapter.itemCount - 1)
    }

    override fun onVideoListLoaded(videos: List<VideoDto>) {
        recyclerAdapter.setItems(videos)
        cachedVideoIdAndTimeElapsedDto?.let {
            selectVideoByVideoId(it)
        }
    }

    fun showAddVideoFragment(videoId: String) {
        AddVideoDialog.newInstance(videoId)
            .show(childFragmentManager, AddVideoDialog.TAG)
    }

    fun playNextVideo() {
        val (nextVideoId, nextVideoPosition) = viewModel.getNextVideoIdAndPosition()
        if (nextVideoId != null && nextVideoPosition != null) {
            selectVideo(nextVideoPosition, nextVideoId)
        } else {
            toast(R.string.player_no_next_video_toast)
            viewModel.optionallyDisconnectBluetooth()
        }
    }

    private fun selectVideo(position: Int, videoId: String, videoSeconds: Float = 0f) {
        viewModel.currentVideoPosition = position
        recyclerAdapter.select(position)
        videos_recycler_view.smoothScrollToPosition(position)
        onVideoSelected(videoId, videoSeconds)
    }

    fun getRemainingVideos() =
        viewModel.getRemainingVideos()

    fun selectVideoByVideoId(videoIdAndTimeElapsedDto: VideoIdAndTimeElapsedDto) {
        val videoPosition = viewModel.getVideoPositionByVideoId(videoIdAndTimeElapsedDto.videoId)
        if (videoPosition == null) {
            cachedVideoIdAndTimeElapsedDto = videoIdAndTimeElapsedDto
        } else {
            selectVideo(
                videoPosition,
                videoIdAndTimeElapsedDto.videoId,
                videoIdAndTimeElapsedDto.timeElapsed
            )
        }

    }
}
