package com.example.youtubebackgroundplayer.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.data.dto.VideoIdAndPositionDto
import java.text.SimpleDateFormat
import java.util.*

class VideosRecyclerViewAdapter(
    private val onItemClick: (videoId: String) -> Unit
) : RecyclerView.Adapter<VideosRecyclerViewAdapter.ViewHolder>() {

    private val items = mutableListOf<VideoDto>()
    private var selectedItemIndex: Int? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_video, parent, false),
            onItemClick = { dto ->
                val selectedVideoId = select(items.indexOf(dto))
                onItemClick(selectedVideoId)
            },
            onDeleteClick = { dto ->
                removeItem(items.indexOf(dto))
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], position, selectedItemIndex)
    }

    fun setItems(itemsToSet: List<VideoDto>) {
     //   this.items.clear()
        items.addAll(itemsToSet)
        notifyDataSetChanged()
    }

    fun addItem(item: VideoDto) {
        items.add(item)
        notifyItemInserted(this.items.size)
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)
        if (selectedItemIndex == position) {
            selectedItemIndex = null
        }
        notifyItemRemoved(position)
    }

    private fun select(position: Int): String {
        val prevSelectedItem = selectedItemIndex
        selectedItemIndex = position
        notifyItemChanged(position)
        if (prevSelectedItem != null) {
            notifyItemChanged(prevSelectedItem)
        }
        return items[position].videoId
    }

    fun selectNextItem(): VideoIdAndPositionDto {
        val nextItemIndex = selectedItemIndex?.plus(1)
        return if (nextItemIndex != null && nextItemIndex < items.size) {
            select(nextItemIndex)
            VideoIdAndPositionDto(items[nextItemIndex].videoId, nextItemIndex)
        } else {
            VideoIdAndPositionDto(null, null)
        }
    }

    class ViewHolder(
        itemView: View,
        private val onItemClick: (dto: VideoDto) -> Unit,
        private val onDeleteClick: (dto: VideoDto) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val durationFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        private val context = itemView.context

        fun bindItem(videoDto: VideoDto, position: Int, selectedItemIndex: Int?) {
            val titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
            val durationTextView  = itemView.findViewById<TextView>(R.id.duration_text_view)
            val rootLayout = itemView.findViewById<View>(R.id.root_layout)
            val deleteImageButton = itemView.findViewById<ImageButton>(R.id.delete_image_button)
            titleTextView.text = videoDto.title
            videoDto.duration?.let { duration ->
                durationTextView.text = durationFormatter.format(Date(duration.millis))
            } ?: run {
                durationTextView.text = context.getString(R.string.playlist_video_no_duration)
            }
            if (videoDto.isWatched) {
                titleTextView.setTextColor(context.getColor(R.color.colorGreen))
            } else {
                titleTextView.setTextColor(context.getColor(R.color.colorWhite))
            }
            deleteImageButton.setOnClickListener {
                onDeleteClick(videoDto)
            }
            itemView.setOnClickListener {
                onItemClick(videoDto)
            }
            selectedItemIndex?.let { selectedIndex ->
                if (position == selectedIndex) {
                    rootLayout.setBackgroundColor(context.getColor(R.color.colorDarkGrey))
                } else {
                    rootLayout.setBackgroundColor(context.getColor(R.color.colorLightBlack))
                }
            } ?: run {
                rootLayout.setBackgroundColor(context.getColor(R.color.colorLightBlack))
            }
        }
    }
}