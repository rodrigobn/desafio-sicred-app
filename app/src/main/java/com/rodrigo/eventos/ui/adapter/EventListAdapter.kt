package com.rodrigo.eventos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.databinding.EventItemBinding

class EventListAdapter(private val listener: OnItemClickListener) : ListAdapter<Event, EventListAdapter.UserViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentEvent: Event = getItem(position)
        holder.binding.event = currentEvent
        holder.binding.executePendingBindings()

        holder.itemView.setOnClickListener {
            listener.onEventClick(currentEvent)
        }
    }

    class UserViewHolder(val binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onEventClick(event: Event)
    }
}