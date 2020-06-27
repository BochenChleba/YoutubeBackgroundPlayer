package com.example.youtubebackgroundplayer.ui.playlist

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndPositionDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val videosRepository: VideosRepository
): BaseViewModel<PlaylistNavigator>() {

    private var cachedVideosList: MutableList<VideoDto>? = null
    var currentVideoPosition: Int? = null

    fun loadVideoList() {
        viewModelScope.launch {
            if (cachedVideosList == null) {
                val videosList = videosRepository.getVideosList()
                cachedVideosList = videosList.toMutableList()
            }
            navigator.onVideoListLoaded(cachedVideosList ?: emptyList())
        }
    }

    fun addVideoToCachedList(videoDto: VideoDto) =
        cachedVideosList?.add(videoDto)

    fun removeVideoFromCachedList(position: Int) {
        cachedVideosList?.removeAt(position)
        currentVideoPosition?.let { currentPosition ->
            if (currentPosition >= position) {
                currentVideoPosition = currentPosition - 1
            }
        }
    }

    fun getNextVideoIdAndPosition(): VideoIdAndPositionDto {
        currentVideoPosition = currentVideoPosition?.plus(1)
        val nextVideoDto = currentVideoPosition?.let { currentPos ->
            if (currentPos < cachedVideosList?.size ?: 0) {
                cachedVideosList?.get(currentPos)
            } else {
                null
            }
        }
        return VideoIdAndPositionDto(nextVideoDto?.videoId, currentVideoPosition)
    }

    fun clearPlaylist() {
        viewModelScope.launch {
            videosRepository.clearAllVideos()
            navigator.showToast(R.string.playlist_cleared_toast)
        }
    }

    fun savePlaylist() {
        cachedVideosList?.let { videosList ->
            viewModelScope.launch {
                videosRepository.setVideos(videosList)
                navigator.showToast(R.string.playlist_saved_toast)
            }
        }
    }

    fun optionallyDisconnectBluetooth() {
        if (preferences.disconnectBluetooth) {
            BluetoothAdapter.getDefaultAdapter().disable()
        }
    }
}
