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
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_attendee_list.*
import kotlinx.android.synthetic.main.fragment_attendee_list.view.*
import kotlin.math.E


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
    private var cardViewHolderSize: Point? = null

    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)
    private val eventsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.EVENTS_COLLECTION)

    private lateinit var eventId: String

    init {
//        this.addEventSnapshotListener()
//        this.addUserSnapshotListener()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eventId = it.getString(Constants.ARG_EVENT_ID)
            Log.d(Constants.TAG, "event ID : $eventId")
        }

    }

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


        cardViewHolderSize = Point(windowSize.x, windowSize.y - bottomMargin)

        eventsRef.document(eventId).get().addOnSuccessListener { snapshot ->
            //val userMap: MutableMap<String,Int> = snapshot["userMap"] as MutableMap<String, Int>
            if (snapshot.toObject(Event::class.java) != null) {
                val tempUser = snapshot.toObject(Event::class.java)
                val userMap = tempUser!!.userMap
                for (applicantId: String in userMap.keys) {
                    if (userMap[applicantId] as Int == Constants.APPLIED) {
                        val user: User? = getUserFromId(applicantId)
                        if (user != null) {
                            view!!.swipe_on_user.addView(UserCard(this, user, cardViewHolderSize!!))
                        } else {
                            Log.d(Constants.TAG, "User $user is NULL")
                        }
                    }
                }
            }
        }

        for (user in Utils.getSampleUsers()) {
            view.swipe_on_user.addView(UserCard(this, user, cardViewHolderSize!!))
        }

        view.rejectBtn2.setOnClickListener({ view.swipe_on_user!!.doSwipe(false) })

        view.acceptBtn2.setOnClickListener({ view.swipe_on_user!!.doSwipe(true) })

        view.undoBtn2.setOnClickListener({ view.swipe_on_user!!.undoLastSwipe() })

        view.swipe_on_user!!.addItemRemoveListener {
            if (isToUndo) {
                isToUndo = false
                view.swipe_on_user!!.undoLastSwipe()
            }
        }

        return view
    }

    fun getUserFromId(id: String): User? {
        var returnUser: User? = null
        userRef.document(id).get().addOnSuccessListener { snapshot ->
            if (snapshot.toObject(User::class.java) != null) {
                returnUser = snapshot.toObject(User::class.java)!!
            }
        }
        return returnUser
    }

    fun getEventFromId(id: String): Event? {
        var returnEvent: Event? = null
        eventsRef.document(id).get().addOnSuccessListener { snapshot ->
            if (snapshot.toObject(Event::class.java) != null) {
                returnEvent = snapshot.toObject(Event::class.java)!!
            }
        }
        return returnEvent
    }

    fun onSwipeRight(user: User) {

//        user.eventMap[eventId] = Constants.ACCEPTED
//        userRef.document(user.userID).set(user)
//        Log.d(Constants.ATTENDEE_SWIPE, "User ${user.fullName}'s  Current Event Mapping: ${user.eventMap[eventId]}")
//
//        val event = getEventFromId(eventId)
//        event!!.userMap[user.userID] = Constants.ACCEPTED
//        eventsRef.document(event.eventID).set(event)
//        Log.d(Constants.ATTENDEE_SWIPE, "Event ${event.title}'s  Current User Mapping: ${event.userMap[user.userID]}")


    }

    fun onSwipeLeft(user: User) {

//        user.eventMap[eventId] = Constants.DENIED
//        userRef.document(user.userID).set(user)
//        Log.d(Constants.ATTENDEE_SWIPE, "User ${user.fullName}'s  Current Event Mapping: ${user.eventMap[eventId]}")
//
//        val event = getEventFromId(eventId)
//        event!!.userMap[user.userID] = Constants.DENIED
//        eventsRef.document(event.eventID).set(event)
//        Log.d(Constants.ATTENDEE_SWIPE, "Event ${event.title}'s  Current User Mapping: ${event.userMap[user.userID]}")

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
