package com.example.youtubebackgroundplayer.ui.playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndOrderDto
import com.example.youtubebackgroundplayer.ui.abstraction.BaseFragment
import com.example.youtubebackgroundplayer.ui.addvideo.AddVideoDialog
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class PlaylistFragment: BaseFragment<PlaylistViewModel>(), KoinComponent, PlaylistNavigator {

    override val viewModel: PlaylistViewModel by viewModel()
    lateinit var onVideoSelected: (VideoIdAndOrderDto) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_playlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        context?.let { ctx ->
            setClickListeners()
            setRecycler(ctx)
        }
        viewModel.loadVideoList()
    }

    private fun setClickListeners() {
        add_button.setOnClickListener {
            AddVideoDialog.newInstance()
                .show(childFragmentManager, AddVideoDialog.TAG)
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddVideoDialog) {
            childFragment.videoSavedCallback = { videoDto ->
                adapter.addItem(videoDto)
                viewModel.addVideoToCachedList(videoDto)
            }
        }
    }

    private val adapter = VideosRecyclerViewAdapter(
        onItemClick = { position ->
            viewModel.getVideoIdAndOrderByPosition(position)
                ?.let { videoIdAndOrder ->
                    onVideoSelected(videoIdAndOrder)
                }
        },
        onDeleteClick = { position ->
            toast(position.toString())
        }
    )

    private fun setRecycler(context: Context) {
        videos_recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        videos_recycler_view.adapter = adapter
    }

    override fun onVideoListLoaded(videos: List<VideoDto>) {
        adapter.setItems(videos)
    }

    fun showAddVideoFragment(videoId: String) {
        AddVideoDialog.newInstance(videoId)
            .show(childFragmentManager, AddVideoDialog.TAG)
    }

    fun selectItemInPlaylist(order: Int) {
        adapter.select(order)
    }
}
