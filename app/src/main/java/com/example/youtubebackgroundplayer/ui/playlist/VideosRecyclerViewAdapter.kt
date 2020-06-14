package com.example.youtubebackgroundplayer.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto

class VideosRecyclerViewAdapter(
    private val onItemClick: (videoId: String, position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<VideosRecyclerViewAdapter.ViewHolder>() {

    private val items = mutableListOf<VideoDto>()
    private var selectedItemPosition: Int? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_video, parent, false),
            onItemClick = { position ->
                select(position)
                onItemClick(items[position].videoId, position)
            },
            onDeleteClick = { position ->
                removeItem(position)
                onDeleteClick(position)
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], position, selectedItemPosition)
    }

    fun setItems(itemsToSet: List<VideoDto>) {
        items.addAll(itemsToSet)
        notifyDataSetChanged()
    }

    fun addItem(item: VideoDto) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun removeItem(position: Int) {
        if (selectedItemPosition == position) {
            selectedItemPosition = null
        }
        selectedItemPosition?.let { selectedPos ->
            if (position < selectedPos) {
                selectedItemPosition = selectedPos - 1
            }
        }
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size - position)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    fun select(position: Int) {
        val prevSelectedItem = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(position)
        if (prevSelectedItem != null) {
            notifyItemChanged(prevSelectedItem)
        }
    }

    class ViewHolder(
        itemView: View,
        private val onItemClick: (position: Int) -> Unit,
        private val onDeleteClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context

        fun bindItem(videoDto: VideoDto, position: Int, selectedItemIndex: Int?) {
            val titleTextView = itemView.findViewById<TextView>(R.id.title_text_view)
            val durationTextView  = itemView.findViewById<TextView>(R.id.duration_text_view)
            val rootLayout = itemView.findViewById<View>(R.id.root_layout)
            val deleteImageButton = itemView.findViewById<ImageButton>(R.id.delete_image_button)
            titleTextView.text = videoDto.title
            videoDto.duration?.let { duration ->
                durationTextView.text = duration.displayableTime
            } ?: run {
                durationTextView.text = context.getString(R.string.playlist_video_no_duration)
            }
            if (videoDto.isWatched) {
                titleTextView.setTextColor(context.getColor(R.color.colorGreen))
            } else {
                titleTextView.setTextColor(context.getColor(R.color.colorWhite))
            }
            deleteImageButton.setOnClickListener {
                onDeleteClick(position)
            }
            itemView.setOnClickListener {
                onItemClick(position)
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