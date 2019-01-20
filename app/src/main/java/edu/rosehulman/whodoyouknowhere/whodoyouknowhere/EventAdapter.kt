package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class EventAdapter(val context: Context, val viewManager: LinearLayoutManager) :
    RecyclerView.Adapter<EventViewHolder>(), ItemTouchHelperAdapter {

    private val eventList = ArrayList<Event>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_event_view, parent, false)
        return EventViewHolder(view,this)
    }

    override fun getItemCount() = eventList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventList[position])
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = eventList.removeAt(fromPosition)
        eventList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        eventList.removeAt(position)
        notifyItemRemoved(position)
    }

}