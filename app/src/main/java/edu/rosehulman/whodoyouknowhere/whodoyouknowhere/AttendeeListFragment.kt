package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mindorks.placeholderview.SwipeDecor
import kotlinx.android.synthetic.main.fragment_attendee_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AttendeeListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AttendeeListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AttendeeListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnEventHostedSelectedListener? = null
    private  val margin  = 160 //160
    private val animationDuration = 300
    private var isToUndo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        for (location in Utils.loadLocations(applicationContext)) {
            swipeView!!.addView(TinderCard(applicationContext, location, cardViewHolderSize, this))
        }

        rejectBtn.setOnClickListener({ swipeView!!.doSwipe(false) })

        acceptBtn.setOnClickListener({ swipeView!!.doSwipe(true) })

        undoBtn.setOnClickListener({ swipeView!!.undoLastSwipe() })

        swipeView!!.addItemRemoveListener {
            if (isToUndo) {
                isToUndo = false
                swipeView!!.undoLastSwipe()
            }
        }
        return inflater.inflate(R.layout.fragment_attendee_list, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        fun onEventHostedSelectedListener(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AttendeeListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
