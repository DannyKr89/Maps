package ru.dk.maps.ui.markers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.dk.maps.data.room.MarkerEntity
import ru.dk.maps.databinding.ItemMarkerBinding


class MarkersAdapter :
    ListAdapter<MarkerEntity, MarkersAdapter.MarkersViewHolder>(MarkersCallback()) {

    var editListener: ((MarkerEntity) -> Unit)? = null
    var deleteListener: ((MarkerEntity) -> Unit)? = null
    var gotoListener: ((MarkerEntity) -> Unit)? = null

    inner class MarkersViewHolder(private val binding: ItemMarkerBinding) :
        ViewHolder(binding.root) {
        fun bind(marker: MarkerEntity) {
            binding.apply {
                markerTitle.text = marker.title
                markerDelete.setOnClickListener {
                    deleteListener?.invoke(marker)
                }
                markerEdit.setOnClickListener {
                    editListener?.invoke(marker)
                }
                markerGoTo.setOnClickListener {
                    gotoListener?.invoke(marker)
                }
            }
        }
    }

    class MarkersCallback : DiffUtil.ItemCallback<MarkerEntity>() {
        override fun areItemsTheSame(oldItem: MarkerEntity, newItem: MarkerEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarkerEntity, newItem: MarkerEntity): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkersViewHolder {
        val binding =
            ItemMarkerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MarkersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}