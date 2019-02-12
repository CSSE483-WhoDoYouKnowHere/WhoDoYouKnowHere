package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mindorks.placeholderview.SwipeDecor
import kotlinx.android.synthetic.main.fragment_attendee_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AttendeeListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AttendeeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

private const val ARG_USERS = "users"

class AttendeeListFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private var listener: OnEventHostedSelectedListener? = null
    private  val margin  = 160 //160
    private val animationDuration = 300
    private var isToUndo = false
    private lateinit var usersList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val test : Bundle = it.getParcelable(ARG_USERS)
            Log.d(Constants.TAG,"fragment has : $test of type ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bottomMargin = Utils.dpToPx(margin)
        val windowSize = Utils.getDisplaySize(activity!!.windowManager)
        swipe_on_user!!.builder
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
                    .setSwipeOutMsgLayoutId(R.layout.card_event_swipe_out))


        val cardViewHolderSize = Point(windowSize.x, windowSize.y - bottomMargin)

        for (users in Utils.getSampleUsers()) {
            swipe_on_user!!.addView(UserCard(users, cardViewHolderSize, activity as UserCard.Callback))
        }

        rejectBtn.setOnClickListener({ swipe_on_user!!.doSwipe(false) })

        acceptBtn.setOnClickListener({ swipe_on_user!!.doSwipe(true) })

        undoBtn.setOnClickListener({ swipe_on_user!!.undoLastSwipe() })

        swipe_on_user!!.addItemRemoveListener {
            if (isToUndo) {
                isToUndo = false
                swipe_on_user!!.undoLastSwipe()
            }
        }
        return inflater.inflate(R.layout.fragment_attendee_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEventHostedSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnEventHostedSelectedListener{
        // TODO: Update argument type and name
        fun onEventHostedSelectedListener(event: Event)
    }

    companion object {

        @JvmStatic
        fun newInstance(users: Bundle) =
            AttendeeListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.ARG_USERS, users)
                }
            }
    }
}
