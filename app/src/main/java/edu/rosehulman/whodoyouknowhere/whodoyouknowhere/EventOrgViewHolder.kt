package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.card_event_view.view.*

class EventOrgViewHolder(itemView: View, val adapter: EventOrgAdapter, val context: Context) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView = itemView.findViewById<TextView>(R.id.event_name_text_view)
    private val locationTextView = itemView.findViewById<TextView>(R.id.event_location_text_view)
    private val dateTextView = itemView.findViewById<TextView>(R.id.event_date_text_view)
//    private val cardView = itemView.card_view

    init {
        itemView.setOnClickListener {
            adapter.showAddEditDialog(adapterPosition)
        }
    }

    fun bind(event: Event) {
        titleTextView.text = event.title
        locationTextView.text = event.location
        dateTextView.text = event.date

    }
}