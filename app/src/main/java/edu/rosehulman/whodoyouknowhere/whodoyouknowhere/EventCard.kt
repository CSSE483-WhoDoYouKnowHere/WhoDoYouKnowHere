package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Point
import android.provider.ContactsContract
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.mindorks.placeholderview.SwipeDirection
import com.mindorks.placeholderview.annotations.*
import com.mindorks.placeholderview.annotations.swipe.*
import kotlin.math.sqrt


@Layout(R.layout.card_event_swipe_view)
class TinderCard(private val context: Context,
                 private val profile: ContactsContract.Profile,
                 private val cardViewHolderSize: Point,
                 private val callback: Callback) {

    @View(R.id.cardSwipeView_Image)
    lateinit var profileImageView: ImageView

    @View(R.id.event_name_text_view)
    lateinit var eventNameText: TextView

    @View(R.id.event_date_text_view)
    lateinit var eventDateText: TextView

    @View(R.id.event_location_text_view)
    lateinit var eventLocationText: TextView

    @SwipeView
    lateinit var swipeView: android.view.View

    @JvmField
    @Position
    var position: Int = 0;

    @Resolve
    fun onResolved() {
//        Picasso.get()
//            .load(mUrl)
//            .resize(800, 800)
//            .centerInside()
//            .into(profileImageView)
//        eventNameText.text = "${profile.name},  ${profile.age}"
//        eventLocationText.text = profile.location
        eventDateText.text= System.currentTimeMillis().toString()
        swipeView.alpha = 1f
    }

    @Click(R.id.cardSwipeView_Image)
    fun onClick() {
        Log.d("EVENT", "profileImageView click")
    }

    @SwipeOutDirectional
    fun onSwipeOutDirectional(direction: SwipeDirection) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name)
        if (direction.direction == SwipeDirection.TOP.direction) {
            callback.onSwipeUp()
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

        val cardHolderDiagonalLength =
            sqrt(Math.pow(cardViewHolderSize.x.toDouble(), 2.0)
                    + (Math.pow(cardViewHolderSize.y.toDouble(), 2.0)))
        val distance = sqrt(Math.pow(xCurrent.toDouble() - xStart.toDouble(), 2.0)
                + (Math.pow(yCurrent.toDouble() - yStart, 2.0)))

        val alpha = 1 - distance / cardHolderDiagonalLength

        Log.d("DEBUG", "onSwipeTouch "
                + " xStart : " + xStart
                + " yStart : " + yStart
                + " xCurrent : " + xCurrent
                + " yCurrent : " + yCurrent
                + " distance : " + distance
                + " TotalLength : " + cardHolderDiagonalLength
                + " alpha : " + alpha
        )

        swipeView.alpha = alpha.toFloat();
    }

    interface Callback {
        fun onSwipeUp()
    }
}