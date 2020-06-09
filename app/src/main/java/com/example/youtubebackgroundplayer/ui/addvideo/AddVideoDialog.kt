package com.example.youtubebackgroundplayer.ui.addvideo

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.ext.hideKeyboard
import com.example.youtubebackgroundplayer.ext.showKeyboard
import com.example.youtubebackgroundplayer.ui.abstraction.BaseDialog
import kotlinx.android.synthetic.main.dialog_add_video.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class AddVideoDialog : BaseDialog(), KoinComponent, AddVideoNavigator {
    companion object {
        private const val ARG_VIDEO_URL = "videoId"
        const val TAG = "AddVideoDialog"

        fun newInstance(videoUrl: String? = null) =
            AddVideoDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_VIDEO_URL, videoUrl)
                }
            }
    }

    lateinit var onVideoAdded: (VideoDto) -> Unit
    private val viewModel: AddVideoViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_add_video, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        setVideoUrl(arguments?.getString(ARG_VIDEO_URL))
        setClickListeners()
        showKeyboard(video_id_edit_text)
    }

    private fun setClickListeners() {
        add_button.setOnClickListener {
            val input = video_id_edit_text.text.toString()
            viewModel.parseInputToVideoId(input)
        }
        cancel_button.setOnClickListener {
            dismiss()
        }
    }

    private fun setVideoUrl(videoUrl: String?) {
        if (videoUrl != null) {
            video_id_edit_text.setText(videoUrl)
        }
    }

    override fun onInvalidInput() {
        toast(getString(R.string.add_video_invalid_input_toast))
    }

    override fun onVideoIdParsed(videoId: String) {
        hideKeyboard(video_id_edit_text)
        viewModel.fetchVideoData(videoId)
    }

    override fun onVideoDataFetched(videoDto: VideoDto) {
        onVideoAdded(videoDto)
        dismiss()
    }

    override fun onVideoDataFetchFailure(videoId: String) {
        // todo show message and request for a title
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        hideKeyboard(video_id_edit_text)
    }
}