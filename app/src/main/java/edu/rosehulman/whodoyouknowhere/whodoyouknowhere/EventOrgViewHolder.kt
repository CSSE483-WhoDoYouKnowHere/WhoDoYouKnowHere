package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_user_view.view.*

class EventOrgViewHolder(itemView: View, val adapter: EventOrgAdapter, val context: Context) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView = itemView.findViewById<TextView>(R.id.event_name_text_view)
    private val locationTextView = itemView.findViewById<TextView>(R.id.event_location_text_view)
    private val dateTextView = itemView.findViewById<TextView>(R.id.event_date_text_view)
    private val imageView = itemView.findViewById<ImageView>(R.id.event_org_image)
//    private val cardView = itemView.card_view

    init {
        itemView.setOnClickListener {
            adapter.selectEventHosted(adapterPosition)

        }
        itemView.setOnLongClickListener{
            adapter.showAddEditDialog(adapterPosition)
            true

        }
    }

    fun bind(event: Event) {
        titleTextView.text = event.title
        locationTextView.text = event.location
        dateTextView.text = event.date
        if(event.picUrl != "") {
            Picasso.get().load(event.picUrl).resize(100, 100).centerCrop().into(imageView)
        } else {
            imageView.setImageResource(R.drawable.louvre)
        }


    }
}