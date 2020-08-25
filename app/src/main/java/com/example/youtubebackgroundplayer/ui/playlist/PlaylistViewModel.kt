package com.example.youtubebackgroundplayer.ui.playlist

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndPositionDto
import com.example.youtubebackgroundplayer.data.repository.VideosRepository
import com.example.youtubebackgroundplayer.ext.minusOneToNull
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

    fun addVideosToCachedList(videoDtoList: List<VideoDto>) {
        cachedVideosList?.addAll(videoDtoList)
        savePlaylistState()
    }

    fun removeVideoFromCachedList(position: Int) {
        cachedVideosList?.removeAt(position)
        currentVideoPosition?.let { currentPosition ->
            if (currentPosition >= position) {
                currentVideoPosition = currentPosition - 1
            }
        }
        savePlaylistState()
    }

    fun updatePlaylistState(videos: List<VideoDto>, selectedPosition: Int?) {
        currentVideoPosition = selectedPosition
        cachedVideosList = videos.toMutableList()
        savePlaylistState()
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
            cachedVideosList?.clear()
            videosRepository.clearAllVideos()
            navigator.showToast(R.string.playlist_cleared_toast)
        }
    }

    private fun savePlaylistState() {
        cachedVideosList?.let { videosList ->
            viewModelScope.launch {
                videosRepository.setVideos(videosList)
            }
        }
    }

    fun optionallyDisconnectBluetooth() {
        if (preferences.disconnectBluetooth) {
            BluetoothAdapter.getDefaultAdapter().disable()
        }
    }

    fun getRemainingVideos() =
        cachedVideosList?.let { videosList ->
            val remainingVideosCount = videosList.size - (currentVideoPosition ?: 0)
            videosList
                .takeLast(remainingVideosCount)
                .map { it.videoId }
        }

    fun getVideoPositionByVideoId(videoId: String) =
        cachedVideosList?.let { videoList ->
            videoList.indexOfFirst { it.videoId == videoId }
        }?.minusOneToNull()
}
