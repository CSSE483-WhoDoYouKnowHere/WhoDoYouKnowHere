package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.*
import com.mindorks.placeholderview.SwipeDecor
import kotlinx.android.synthetic.main.card_user_view.view.*
import kotlinx.android.synthetic.main.fragment_attendee_list.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM2 = "param2"

class AttendeeListFragment : Fragment() {


    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private val margin = 160 //160
    private val animationDuration = 300
    private var isToUndo = false
    private var swipedUser: User? = null
    private var addingUser: Boolean = true
    private var acceptedList = ArrayList<User>()
    private var deniedList = ArrayList<User>()
    private var applicantList = ArrayList<User>()

    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)

    private lateinit var eventId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eventId = it.getString(Constants.ARG_EVENT_ID)
            Log.d(Constants.TAG, "event ID : $eventId")
        }

    }

    fun addEventSnapshotListener() {
        eventsRef.orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, fireStoreException ->
                if (fireStoreException != null) {
                    Log.d(Constants.TAG, "Firebase error: $fireStoreException")
                    return@addSnapshotListener
                }

                processEventSnapshotDiffs(snapshot!!)

            }
    }

//    fun addUserSnapshotListener() {
//        userRef.orderBy("timeStamp", Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshot, fireStoreException ->
//                if (fireStoreException != null) {
//                    Log.d(Constants.TAG, "Firebase error: $fireStoreException")
//                    return@addSnapshotListener
//                }
//
//                processUserSnapshotDiffs(snapshot!!)
//
//            }
//    }


    init {
        this.addEventSnapshotListener()
//        this.addUserSnapshotListener()
    }


    private fun processEventSnapshotDiffs(snapshot: QuerySnapshot) {
        for (docChange in snapshot.documentChanges) {
            val event = Event.fromSnapshot(docChange.document)
            when (docChange.type) {
                DocumentChange.Type.ADDED -> {
                    //don't care
                }
                DocumentChange.Type.REMOVED -> {
                    //don't care
                }
                DocumentChange.Type.MODIFIED -> {
                    if (addingUser) {
                        Log.d(Constants.TAG, "$swipedUser added to the accepted list")
                        // val acceptedPosition = acceptedList.indexOfFirst { event.id == it.id }
                        // event.acceptedList[acceptedPosition] = swipedUser!!
                        if (eventId == event.id) {
                            event.acceptedList.add(swipedUser!!)
                            event.applicantList.remove(swipedUser!!)
                        }
                    } else if (!addingUser) {
                        val deniedPosition = deniedList.indexOfFirst { event.id == it.id }
                        event.deniedList[deniedPosition] = swipedUser!!
                        event.applicantList.remove(swipedUser!!)
                        Log.d(Constants.TAG, "$swipedUser added to denied list")

                    }
                }
            }
        }
    }

//    private fun processUserSnapshotDiffs(snapshot: QuerySnapshot) {
//        for (docChange in snapshot.documentChanges) {
//            val user = User.fromSnapshot(docChange.document)
//            when (docChange.type) {
//                DocumentChange.Type.ADDED -> {
//                    //don't care
//                }
//                DocumentChange.Type.REMOVED -> {
//                    //don't care
//                }
//                DocumentChange.Type.MODIFIED -> {
//                    var event = Event()
//                    eventsRef.document(eventId).get().addOnSuccessListener { snapshot: DocumentSnapshot ->
//                        event = snapshot.toObject(Event::class.java) ?: Event()
//                    }
//                    if (addingUser) {
//                        Log.d(Constants.TAG, "$swipedUser added to the accepted list")
//                        user.eventsAcceptedTo.add(event)
//                        user.eventsAppliedTo.remove(event)
//                    } else if (!addingUser) {
//                        user.eventsDeniedFrom.add(event)
//                        user.eventsAppliedTo.remove(event)
//
//                    }
//                }
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_attendee_list, container, false)

        val bottomMargin = Utils.dpToPx(margin)
        val windowSize = Utils.getDisplaySize(activity!!.windowManager)

        val fab = (context as MainActivity).getFab()
        fab.hide()

        view.swipe_on_user!!.builder
            .setDisplayViewCount(3)
            .setIsUndoEnabled(true)
            .setSwipeVerticalThreshold(Utils.dpToPx(50))
            .setSwipeHorizontalThreshold(Utils.dpToPx(50))
            .setHeightSwipeDistFactor(10f)
            .setWidthSwipeDistFactor(5f)
            .setSwipeDecor(
                SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setSwipeAnimTime(animationDuration)
                    .setRelativeScale(0.01f)
                    .setSwipeInMsgLayoutId(R.layout.card_event_swipe_in)
                    .setSwipeOutMsgLayoutId(R.layout.card_event_swipe_out)
            )


        val cardViewHolderSize = Point(windowSize.x, windowSize.y - bottomMargin)

        for (user in Utils.getSampleUsers()) {


            var userCard = UserCard(this, user, cardViewHolderSize)
            view.swipe_on_user!!.addView(userCard)

        }

//        rejectBtn.setOnClickListener({ view.swipe_on_user!!.doSwipe(false) })
//
//        acceptBtn.setOnClickListener({ view.swipe_on_user!!.doSwipe(true) })
//
//        undoBtn.setOnClickListener({ view.swipe_on_user!!.undoLastSwipe() })

        view.swipe_on_user!!.addItemRemoveListener {
            if (isToUndo) {
                isToUndo = false
                view.swipe_on_user!!.undoLastSwipe()
            }
        }

        return view
    }

    fun onSwipeRight(user: User) {
        swipedUser = user
        addingUser = true
        var event = Event()
        eventsRef.document(eventId).get().addOnSuccessListener { snapshot: DocumentSnapshot ->
            event = snapshot.toObject(Event::class.java) ?: Event()

        }
        event.acceptedList.add(user)
        event.applicantList.remove(user)
        eventsRef.document(eventId).set(event)

//        swipedUser!!.eventsAcceptedTo.add(event)
//        swipedUser!!.eventsAppliedTo.remove(event)
//        userRef.document(swipedUser!!.id).set(swipedUser!!)

    }

    fun onSwipeLeft(user: User) {
        swipedUser = user
        addingUser = false
        var event: Event? = null
        eventsRef.document(eventId).get().addOnSuccessListener { snapshot: DocumentSnapshot ->
            event = snapshot.toObject(Event::class.java)
        }
        Log.d(Constants.TAG, "Event is $event")
        if (event != null) {
            event?.deniedList?.add(user)
            event?.applicantList?.remove(user)
            eventsRef.document(eventId).set(event!!)
        }

//        swipedUser!!.eventsDeniedFrom.add(event)
//        swipedUser!!.eventsAppliedTo.remove(event)
//        userRef.document(swipedUser!!.id).set(swipedUser!!)

    }

    companion object {

        @JvmStatic
        fun newInstance(eventId: String) =
            AttendeeListFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_EVENT_ID, eventId)
                }
            }
    }
}
