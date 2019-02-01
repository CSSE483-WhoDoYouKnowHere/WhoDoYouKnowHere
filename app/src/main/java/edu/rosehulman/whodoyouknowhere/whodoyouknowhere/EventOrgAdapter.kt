package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.provider.CalendarContract
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.add_event_dialog.view.*

class EventOrgAdapter(val context: Context?) : RecyclerView.Adapter<EventOrgViewHolder>() {


    private var events = ArrayList<Event>()



    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: EventOrgViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventOrgViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_event_view, parent, false)
        return EventOrgViewHolder(view, this, context!!)
    }



    fun showAddEditDialog(position: Int = -1) {
        val builder = AlertDialog.Builder(context!!)
        //Configure the builder: title, icon, message/custom view OR list.  Buttons (pos, neg, neutral)
        builder.setTitle("Add an event")
        //TODO:
        val view = LayoutInflater.from(context).inflate(R.layout.add_event_dialog, null, false)
        builder.setView(view)

        // TODO: If editing, pre-populate the edit texts (like Jersey)
        if (position >= 0) {
            //Edit
            builder.setTitle("Edit this event")
            view.add_name_edit_text.setText(events[position].title) //Type of text is editable, not a string
            view.add_date_edit_text.setText(events[position].date)
            view.add_location_edit_text.setText(events[position].location)
        }

        builder.setPositiveButton(android.R.string.ok, { _, _ ->
            val title = view.add_name_edit_text.text.toString()
            val date = view.add_date_edit_text.text.toString()
            val location = view.add_location_edit_text.text.toString()
            if (position >= 0) {
                edit(position, title, date, location)
            } else {
                add(Event(0, title, date, location))
            }

        })
        builder.setNegativeButton(android.R.string.cancel, null) // :)

        if (position >= 0) {
            builder.setNeutralButton("Remove") { _, _ ->
                remove(position)
            }
        }

        builder.create().show()
    }

    fun add(event: Event) {
        events.add(event)
    }

    fun edit(position: Int, title: String, date: String, location: String) {


            val temp = events[position].copy()
            temp.title = title
            temp.date = date
            temp.location = location

            events[position].title = title
            events[position].date = date
            events[position].location = location


            //More?
            notifyItemChanged(position)
    }

    private fun remove(pos: Int) {
        events.removeAt(pos)
        notifyDataSetChanged()
    }


}