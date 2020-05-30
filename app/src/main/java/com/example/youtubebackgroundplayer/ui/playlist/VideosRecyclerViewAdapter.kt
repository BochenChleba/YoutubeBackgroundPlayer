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
import java.text.SimpleDateFormat
import java.util.*

class VideosRecyclerViewAdapter(
    private val onItemClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<VideosRecyclerViewAdapter.ViewHolder>() {

    private val items = mutableListOf<VideoDto>()
    private var selectedItemIndex: Int? = null

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_item_video, parent, false),
            onItemClick = { position ->
                select(position)
                onItemClick(position)
            },
            onDeleteClick = onDeleteClick
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], position, selectedItemIndex)
    }

    fun setItems(items: List<VideoDto>) {
     //   this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: VideoDto) {
        this.items.add(item)
        notifyItemInserted(this.items.size)
    }

    fun select(position: Int) {
        val prevSelectedItem = this.selectedItemIndex
        this.selectedItemIndex = position
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
            }
        }
    }
}