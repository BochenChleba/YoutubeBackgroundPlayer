package com.example.youtubebackgroundplayer.ui.addvideo

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.databinding.DialogAddVideoBinding
import com.example.youtubebackgroundplayer.ext.hideKeyboard
import com.example.youtubebackgroundplayer.ext.showKeyboard
import com.example.youtubebackgroundplayer.ui.abstraction.BaseDialog
import kotlinx.android.synthetic.main.dialog_add_video.*
import kotlinx.android.synthetic.main.layout_accept_cancel_buttons.*
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

    lateinit var onVideosAdded: (List<VideoDto>) -> Unit
    private val viewModel: AddVideoViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? =
        DataBindingUtil.inflate<DialogAddVideoBinding>(
            inflater,
            R.layout.dialog_add_video,
            container,
            false
        )
            .also { binding -> binding.lifecycleOwner = this }
            .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavigator(this)
        setVideoUrl(arguments?.getString(ARG_VIDEO_URL))
        setClickListeners()
        showKeyboard(input_edit_text)
    }

    private fun setClickListeners() {
        accept_button.setOnClickListener {
            val input = input_edit_text.text.toString()
            viewModel.parseInputAndFetchData(input)
        }
        cancel_button.setOnClickListener {
            dismiss()
        }
    }

    private fun setVideoUrl(videoUrl: String?) {
        if (videoUrl != null) {
            input_edit_text.setText(videoUrl)
        }
    }

    override fun onInvalidInput() {
        toast(getString(R.string.add_video_invalid_input_toast))
    }

    override fun onInputParsed() {
        accept_button.isClickable = false
        //todo progressbar
    }

    override fun onVideoDataFetched(videoDtoList: List<VideoDto>) {
        hideKeyboard(input_edit_text)
        onVideosAdded(videoDtoList)
        dismiss()
    }

    override fun onVideoDataFetchFailure(videoId: String) {
        input_edit_text.setText("")
        content_text_view.text = getString(R.string.add_video_enter_title_prompt)
        toast(getString(R.string.add_video_failure_toast))
        accept_button.setOnClickListener {
            val title = input_edit_text.text?.toString()
                .also {
                    if (it.isNullOrEmpty()) {
                        toast(getString(R.string.add_video_add_title_toast))
                        return@setOnClickListener
                    }
                }
            val videoDto = VideoDto(
                title = title!!,
                videoId = videoId
            )
            onVideoDataFetched(listOf(videoDto))
        }
    }

    override fun onPlaylistFetchFailure() {
        toast(getString(R.string.add_playlist_failure_toast))
        //todo remove and use showToast
    }

    override fun onDataFetchFinished() {
        accept_button.isClickable = true
        //todo progressbar
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        hideKeyboard(input_edit_text)
    }
}