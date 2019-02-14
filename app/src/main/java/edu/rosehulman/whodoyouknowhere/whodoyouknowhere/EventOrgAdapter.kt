package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.add_event_dialog.view.*


class EventOrgAdapter(
    val context: Context?,
    var uid: String,
    val listener: EventOrgFragment.OnEventOrgFragmentSelectedListener?,
    var userId: String
) : RecyclerView.Adapter<EventOrgViewHolder>(), ItemTouchHelperAdapter {


    private val user = FirebaseAuth.getInstance().currentUser
    //private var eventsHostedMap: HashMap<String, Boolean>? = null
    private var eventsHostedList = ArrayList<Event>()
    private var eventMap: MutableMap<String, Int>? = null
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)
//        .document(uid)


    init {
        uid = user!!.uid
        Log.d(Constants.TAG, "UID is: $uid")

        //Get the events hosted map from the user
        userRef.document(userId).get().addOnSuccessListener { document: DocumentSnapshot ->
            eventMap = document["eventMap"] as MutableMap<String, Int>? ?: mutableMapOf<String, Int>()
            val tempUser = document.toObject(User::class.java)
            tempUser!!.eventMap = eventMap!!
            userRef.document(userId).set(tempUser)
        }

        //Get the eventsHostedList list
//        for(keyID in eventsHostedMap.keys){
//            eventsRef.document(keyID).get().addOnSuccessListener { document: DocumentSnapshot ->
//                eventsHostedList.add(Event.fromSnapshot(document))
//            }
//        }

        this.addEventSnapshotListener()
//        this.addUserSnapshotListener()

    }

    fun addEventSnapshotListener() {
        eventsRef
            .addSnapshotListener { snapshot, fireStoreException ->
                if (fireStoreException != null) {
                    Log.d(Constants.TAG, "Firebase error: $fireStoreException")
                    return@addSnapshotListener
                }
                processEventSnapshotDiffs(snapshot!!)

            }
    }


    private fun processEventSnapshotDiffs(snapshot: QuerySnapshot) {
        for (docChange in snapshot.documentChanges) {
            val event = Event.fromSnapshot(docChange.document)
            when (docChange.type) {
                DocumentChange.Type.ADDED -> {
                    Log.d(Constants.TAG, "Event: $event added in EventOrgAdapter")
                    eventsHostedList.add(0, event)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    Log.d(Constants.TAG, "Event: $event removed in EventOrgAdapter")
                    val position = eventsHostedList.indexOf(event)
                    eventsHostedList.removeAt(position)
                    notifyItemRemoved(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    Log.d(Constants.TAG, "Event: $event modified in EventOrgAdapter")
                    val position = eventsHostedList.indexOfFirst { event.id == it.id }
                    eventsHostedList[position] = event
                    notifyItemChanged(position)
                }
            }
        }
    }


    fun showAddEditDialog(index: Int = -1) {
        var picUrl = ""
        val builder = AlertDialog.Builder(context!!)
        //Configure the builder: title, icon, message/custom view OR list.  Buttons (pos, neg, neutral)
        builder.setTitle("Add an event")
        val view = LayoutInflater.from(context).inflate(R.layout.add_event_dialog, null, false)
        builder.setView(view)

        if (index >= 0) {
            //Edit
            builder.setTitle("Edit this event")
            view.add_name_edit_text.setText(eventsHostedList[index].title) //Type of text is editable, not a string
            view.add_date_edit_text.setText(eventsHostedList[index].date)
            view.add_location_edit_text.setText(eventsHostedList[index].location)
            view.add_picture_edit_text.setText(eventsHostedList[index].picUrl)
        }

        builder.setPositiveButton(android.R.string.ok, { _, _ ->
            val title = view.add_name_edit_text.text.toString()
            val date = view.add_date_edit_text.text.toString()
            val location = view.add_location_edit_text.text.toString()
            picUrl =
                if (!view.add_picture_edit_text.text.toString().equals("")) view.add_picture_edit_text.text.toString() else Utils.getSampleEventUrl()
            if (index >= 0) {
                edit(index, title, date, location)

            } else {
//                add(Event("", title, date, location, picUrl))
                add(title, date, location, picUrl)
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

    //    fun add(event: Event) {
    fun add(title: String, date: String, location: String, picUrl: String) {
        val docRef = eventsRef.document()
        val event = Event(docRef.id, title, date, location, picUrl)
        event.userMap[userId]=Constants.HOSTING

        docRef.set(event).addOnSuccessListener {

            setUsersEventMapTo(event, Constants.HOSTING)
        }
//        eventsRef.add(event).addOnSuccessListener {
//            Log.d(Constants.ARG_EVENT_ID, "Event added with ID: ${it.id}")
//            event.eventID = it.id
//            event.userMap[userId]= Constants.HOSTING
//            eventsRef.document(it.id).set(event)
//
//            setUsersEventMapTo(event, Constants.HOSTING)
//
//        }
    }

    fun edit(index: Int, title: String, date: String, location: String) {

        val temp = eventsHostedList[index].copy()
        temp.title = title
        temp.date = date
        temp.location = location

        eventsHostedList[index].title = title
        eventsHostedList[index].date = date
        eventsHostedList[index].location = location


        //More?
        eventsRef.document(eventsHostedList[index].id).set(temp)
    }

    private fun setUsersEventMapTo(event: Event, state: Int) {
        var tempUser: User?

        userRef.document(userId).get().addOnSuccessListener { document: DocumentSnapshot ->
            tempUser = document.toObject(User::class.java)
            Log.d(Constants.ARG_EVENT_ID, "TempUser ${tempUser}")

            tempUser!!.eventMap[event.eventID] = state

            Log.d(Constants.ARG_EVENT_ID, "event ID : ${event.eventID}")
            Log.d(Constants.ARG_EVENT_ID, "eventMap : ${tempUser!!.eventMap}")

            Log.d(Constants.ARG_EVENT_ID, "$tempUser")
            userRef.document(userId).set(tempUser!!)
        }
    }

    private fun getEventFromEventID(eventId: String) {

    }

    private fun getUserFromUserID(userId: String) {

    }

    private fun remove(index: Int) {
        //Delete from the "events" collection in Firebase
        eventsRef.document(eventsHostedList[index].id).delete()

        //Delete from the user's eventsHostedList ArrayList<Event> done in processSnapShotDiffs
        //Or is is...? Only removed locally I think, not in firebase...
        //TODO: Probably add another snapshot listener for the user's eventsHostedList list :-(
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = eventsHostedList.removeAt(fromPosition)
        eventsHostedList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {

        eventsRef.document(eventsHostedList[position].id).delete()

    }

    override fun getItemCount() = eventsHostedList.size

    override fun onBindViewHolder(holder: EventOrgViewHolder, position: Int) {
        holder.bind(eventsHostedList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventOrgViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_event_view, parent, false)
        return EventOrgViewHolder(view, this, context!!)
    }

    fun selectEventHosted(index: Int) {
        listener?.onEventOrgFragmentSelected(eventsHostedList[index])
    }


}