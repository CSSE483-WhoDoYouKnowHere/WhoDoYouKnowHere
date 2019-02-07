package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.mindorks.placeholderview.SwipeDirection
import com.mindorks.placeholderview.annotations.*
import com.mindorks.placeholderview.annotations.swipe.*


//@Layout(R.layout.swipe_placeholder_view)
//class EventCard(
//   val mContext: Context,
//    val event: Event,
//   val  mSwipeView: SwipePlaceHolderView
//) {
//
//    @View(R.id.profileImageView)
//    private val profileImageView: ImageView? = null
//
//    @View(R.id.eventNametxt)
//    private val eventName: TextView? = null
//
//    @View(R.id.locationNameTxt)
//    private val locationNameTxt: TextView? = null
//
//    @Resolve
//    private fun onResolved() {
////        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView)
//        profileImageView?.setBackgroundColor(R.color.material_blue_grey_900)
//        eventName!!.setText(event.title + ", " + event.eventType)
//        locationNameTxt!!.setText(event.location)
//    }
//
//    @SwipeOut
//    private fun onSwipedOut() {
//        Log.d("EVENT", "onSwipedOut")
//       // mSwipeView.add(this)
//    }
//
//    @SwipeCancelState
//    private fun onSwipeCancelState() {
//        Log.d("EVENT", "onSwipeCancelState")
//    }
//
//    @SwipeIn
//    private fun onSwipeIn() {
//        Log.d("EVENT", "onSwipedIn")
//    }
//
//    @SwipeInState
//    private fun onSwipeInState() {
//        Log.d("EVENT", "onSwipeInState")
//    }
//
//    @SwipeOutState
//    private fun onSwipeOutState() {
//        Log.d("EVENT", "onSwipeOutState")
//    }
//}

@NonReusable
@Layout(R.layout.card_event_swipe_view)
class EventCard(private val context: Context,
                 private val event: Event,
                 private val mSwipeView: SwipeView) {

    @View(R.id.profileImageView)
    lateinit var profileImageView: ImageView

    @View(R.id.eventNametxt)
    lateinit var nameAgeTxt: TextView

    @View(R.id.locationNameTxt)
    lateinit var locationNameTxt: TextView

    @SwipeView
    lateinit var swipeView: android.view.View

    @JvmField
    @Position
    var position: Int = 0;

    @Resolve
    fun onResolved() {
//        Glide.with(context).load(profile.imageUrl).bitmapTransform(
//            RoundedCornersTransformation(
//                context,
//                Utils.dpToPx(7),
//                0,
//                RoundedCornersTransformation.CornerType.TOP))
//            .into(profileImageView)
        nameAgeTxt.text = "${event.title},  ${event.date}"
        locationNameTxt.text = event.location
        swipeView.alpha = 1f
    }

    @Click(R.id.profileImageView)
    fun onClick() {
        Log.d("EVENT", "profileImageView click")
    }

    @SwipeOutDirectional
    fun onSwipeOutDirectional(direction: SwipeDirection) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name)
        if (direction.direction == SwipeDirection.TOP.direction) {
          //  callback.onSwipeUp()
        }
    }

    @SwipeCancelState
    fun onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState")
        swipeView.alpha = 1f
    }

    @SwipeInDirectional
    fun onSwipeInDirectional(direction: SwipeDirection) {
        Log.d("DEBUG", "SwipeInDirectional " + direction.name)
    }

    @SwipingDirection
    fun onSwipingDirection(direction: SwipeDirection) {
        Log.d("DEBUG", "SwipingDirection " + direction.name)
    }

    @SwipeTouch
    fun onSwipeTouch(xStart: Float, yStart: Float, xCurrent: Float, yCurrent: Float) {
//        val cardHolderDiagonalLength =
//            sqrt(Math.pow(cardViewHolderSize.x.toDouble(), 2.0)
//                    + (Math.pow(cardViewHolderSize.y.toDouble(), 2.0)))
//        val distance = sqrt(Math.pow(xCurrent.toDouble() - xStart.toDouble(), 2.0)
//                + (Math.pow(yCurrent.toDouble() - yStart, 2.0)))
//
//        val alpha = 1 - distance / cardHolderDiagonalLength

//        Log.d("DEBUG", "onSwipeTouch "
//                + " xStart : " + xStart
//                + " yStart : " + yStart
//                + " xCurrent : " + xCurrent
//                + " yCurrent : " + yCurrent
//                + " distance : " + distance
//                + " TotalLength : " + cardHolderDiagonalLength
//                + " alpha : " + alpha
//        )
//
//        swipeView.alpha = alpha.toFloat();
    }

    interface Callback {
        fun onSwipeUp()
    }
}