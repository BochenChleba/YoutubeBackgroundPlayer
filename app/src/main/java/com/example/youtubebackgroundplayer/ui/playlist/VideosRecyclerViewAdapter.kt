package com.example.youtubebackgroundplayer.ui.playlist

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubebackgroundplayer.R
import com.example.youtubebackgroundplayer.data.dto.VideoDto
import com.example.youtubebackgroundplayer.util.rv.ItemMoveCallbackListener
import java.util.*

class VideosRecyclerViewAdapter(
    private val onItemClick: (videoId: String, position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit,
    private val onStartDrag: (holder: ViewHolder) -> Unit,
    private val onFinishDrag: (items: List<VideoDto>, selectedItemPosition: Int?) -> Unit
) : RecyclerView.Adapter<VideosRecyclerViewAdapter.ViewHolder>(), ItemMoveCallbackListener.Listener {

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
        holder.itemView.setOnLongClickListener {
            onStartDrag(holder)
            true
        }
    }

    fun setItems(itemsToSet: List<VideoDto>) {
        items.addAll(itemsToSet)
        notifyDataSetChanged()
    }

    fun addItems(itemsToAdd: List<VideoDto>) {
        val currentSize = items.size
        items.addAll(itemsToAdd)
        notifyItemRangeInserted(currentSize, itemsToAdd.size)
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

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        if (fromPosition == selectedItemPosition) {
            selectedItemPosition = toPosition
        } else if (toPosition == selectedItemPosition) {
            selectedItemPosition = fromPosition
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowClear(itemViewHolder: ViewHolder) {
        notifyDataSetChanged()
        onFinishDrag(items, selectedItemPosition)
    }

    override fun onRowSelected(itemViewHolder: ViewHolder) {}

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