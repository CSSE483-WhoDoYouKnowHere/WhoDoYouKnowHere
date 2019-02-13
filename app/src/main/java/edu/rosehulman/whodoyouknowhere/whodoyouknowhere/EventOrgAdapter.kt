package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.add_event_dialog.view.*
import kotlin.collections.ArrayList
import android.R.attr.fragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager


class EventOrgAdapter(val context: Context?, var uid: String, val listener: EventOrgFragment.OnEventOrgFragmentSelectedListener?) : RecyclerView.Adapter<EventOrgViewHolder>() {

    private val user = FirebaseAuth.getInstance().currentUser
    private var eventsHosted = ArrayList<Event>()
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)
//        .document(uid)

    //Do we need another events arraylist?-

    init {
        uid = user!!.uid
        this.addEventSnapshotListener()
        Log.d(Constants.TAG, "UID is: $uid")
        //userRef.add(uid)
//        userRef.document(uid).get().addOnSuccessListener { snapShot: DocumentSnapshot ->
//            eventsHosted = snapShot.toObject(User::class.java)?.eventsHosting ?: ArrayList<Event>(0)
//            //What I think it happening: there is no user object stored in the user collection so we are getting an empty array
//            //TODO: Fix
//        }
    }

    fun addEventSnapshotListener() {
        eventsRef.orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, fireStoreException ->
                if (fireStoreException != null) {
                    Log.d(Constants.TAG, "Firebase error: $fireStoreException")
                    return@addSnapshotListener
                }

//            populateLocalQuotes(snapshot!!)
                processEventSnapshotDiffs(snapshot!!)

            }
    }

//    fun addUserSnapshotListener()


    private fun processEventSnapshotDiffs(snapshot: QuerySnapshot) {
        for (docChange in snapshot.documentChanges) {
            val event = Event.fromSnapshot(docChange.document)
            when (docChange.type) {
                DocumentChange.Type.ADDED -> {
                    eventsHosted.add(0, event)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    val position = eventsHosted.indexOf(event)
                    eventsHosted.removeAt(position)
                    notifyItemRemoved(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    //TODO: Look up it in Kotlin
                    val position = eventsHosted.indexOfFirst { event.id == it.id }
                    //This line makes sure local is in sync with the database. WHY?
                    eventsHosted[position] = event
                    notifyItemChanged(position)
                }
            }
        }
    }


    fun showAddEditDialog(index: Int = -1) {
        val builder = AlertDialog.Builder(context!!)
        //Configure the builder: title, icon, message/custom view OR list.  Buttons (pos, neg, neutral)
        builder.setTitle("Add an event")
        //TODO:
        val view = LayoutInflater.from(context).inflate(R.layout.add_event_dialog, null, false)
        builder.setView(view)

        // TODO: If editing, pre-populate the edit texts (like Jersey)
        if (index >= 0) {
            //Edit
            builder.setTitle("Edit this event")
            view.add_name_edit_text.setText(eventsHosted[index].title) //Type of text is editable, not a string
            view.add_date_edit_text.setText(eventsHosted[index].date)
            view.add_location_edit_text.setText(eventsHosted[index].location)
        }

        builder.setPositiveButton(android.R.string.ok, { _, _ ->
            val title = view.add_name_edit_text.text.toString()
            val date = view.add_date_edit_text.text.toString()
            val location = view.add_location_edit_text.text.toString()
            if (index >= 0) {
                edit(index, title, date, location)
            } else {
                add(Event(0, title, date, location))
            }

        })
        builder.setNegativeButton(android.R.string.cancel, null) // :)

        if (index >= 0) {
            builder.setNeutralButton("Remove") { _, _ ->
                remove(index)
            }
        }

        builder.create().show()
    }

    fun add(event: Event) {
        eventsRef.add(event)

    }

    fun edit(index: Int, title: String, date: String, location: String) {

        val temp = eventsHosted[index].copy()
        temp.title = title
        temp.date = date
        temp.location = location

        eventsHosted[index].title = title
        eventsHosted[index].date = date
        eventsHosted[index].location = location


        //More?
        eventsRef.document(eventsHosted[index].id).set(temp)
    }

    private fun remove(index: Int) {
        //Delete from the "events" collection in Firebase
        eventsRef.document(eventsHosted[index].id).delete()

        //Delete from the user's eventsHosted ArrayList<Event> done in processSnapShotDiffs
        //Or is is...? Only removed locally I think, not in firebase...
        //TODO: Probably add another snapshot listener for the user's eventsHosted list :-(
    }


    override fun getItemCount() = eventsHosted.size

    override fun onBindViewHolder(holder: EventOrgViewHolder, position: Int) {
        holder.bind(eventsHosted[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventOrgViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_event_view, parent, false)
        return EventOrgViewHolder(view, this, context!!)
    }

    fun selectEventHosted(index: Int) {
        listener?.onEventOrgFragmentSelected(eventsHosted[index])
    }


}