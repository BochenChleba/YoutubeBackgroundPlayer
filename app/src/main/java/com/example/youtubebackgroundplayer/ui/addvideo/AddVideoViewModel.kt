package com.example.youtubebackgroundplayer.ui.addvideo

import androidx.lifecycle.viewModelScope
import com.example.youtubebackgroundplayer.constant.NetworkConstants.QUERY_SEPARATOR
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_PLAYLIST_PREFIX
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_VIDEO_ID_LENGTH
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_VIDEO_ID_PREFIX_1
import com.example.youtubebackgroundplayer.constant.NetworkConstants.YOUTUBE_VIDEO_ID_PREFIX_2
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.network.YoutubeApiService
import com.example.youtubebackgroundplayer.ui.abstraction.BaseViewModel
import kotlinx.coroutines.launch

class AddVideoViewModel(
    private val youtubeApiService: YoutubeApiService
): BaseViewModel<AddVideoNavigator>() {

    fun parseInputAndFetchData(input: String) =
        when {
            input.length == YOUTUBE_VIDEO_ID_LENGTH -> {
                getNavigator().onInputParsed()
            }
            input.contains(YOUTUBE_VIDEO_ID_PREFIX_1) -> {
                val videoId = getVideoIdFromInput(input, YOUTUBE_VIDEO_ID_PREFIX_1)
                getNavigator().onInputParsed()
                fetchVideoData(videoId)
            }
            input.contains(YOUTUBE_VIDEO_ID_PREFIX_2) -> {
                val videoId = getVideoIdFromInput(input, YOUTUBE_VIDEO_ID_PREFIX_2)
                getNavigator().onInputParsed()
                fetchVideoData(videoId)
            }
            input.contains(YOUTUBE_PLAYLIST_PREFIX) -> {
                val playlistId = getPlaylistIdFromInput(input)
                getNavigator().onInputParsed()
                fetchPlaylistData(playlistId)
            }
            else -> {
                getNavigator().onInvalidInput()
            }
        }

    private fun getVideoIdFromInput(input: String, videoIdPrefix: String) =
        input.substringAfter(videoIdPrefix)
            .take(YOUTUBE_VIDEO_ID_LENGTH)

    private fun getPlaylistIdFromInput(input: String) =
        input.substringAfter(YOUTUBE_PLAYLIST_PREFIX).substringBefore(QUERY_SEPARATOR)

    private fun fetchVideoData(videoId: String) {
        viewModelScope.launch {
            try {
                val response = youtubeApiService.fetchVideoDetails(videoId)
                val videoDto = VideoDto.fromGetVideoDetailsResponse(response)
                getNavigator().onVideoDataFetched(listOf(videoDto))
            } catch (ex: Throwable) {
                ex.printStackTrace()
                getNavigator().onVideoDataFetchFailure(videoId)
            } finally {
                getNavigator().onDataFetchFinished()
            }
        }
    }

    private fun fetchPlaylistData(playlistId: String) {
        viewModelScope.launch {
            try {
                val response = youtubeApiService.fetchPlaylist(playlistId)
                val videoDtoList = VideoDto.fromGetPlaylistResponse(response)
                getNavigator().onVideoDataFetched(videoDtoList)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                getNavigator().onPlaylistFetchFailure()
            } finally {
                getNavigator().onDataFetchFinished()
            }
        }
    }
}
