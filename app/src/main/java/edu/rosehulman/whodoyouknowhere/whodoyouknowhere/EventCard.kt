package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.mindorks.placeholderview.SwipeDirection
import com.mindorks.placeholderview.annotations.*
import com.mindorks.placeholderview.annotations.swipe.*
import com.squareup.picasso.Picasso
import kotlin.math.sqrt


@Layout(R.layout.card_event_swipe_view)
class EventCard(
    private val context: Context,
    private var event: Event,
    private val cardViewHolderSize: Point
) {

    @View(R.id.cardSwipeView_Image)
    lateinit var profileImageView: ImageView

    @View(R.id.event_name_text_view)
    lateinit var eventNameText: TextView
    @View(R.id.event_type_text_view)
    lateinit var eventTypeText: TextView

    @View(R.id.event_desc_text_view)
    lateinit var eventDescText: TextView

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
        if (event.picUrl != "") {
            Picasso.get()
                .load(event.picUrl)
                .resize(900, 900).centerInside()

                .into(profileImageView)
        } else {
            Picasso.get()
                .load(Utils.getSampleEventUrl())
                .resize(900, 900).centerInside()
                .into(profileImageView)
        }
//        eventNameText.text = "${profile.name},  ${profile.age}"
        eventNameText.text = event.title
        eventLocationText.text = event.location
        eventDateText.text = System.currentTimeMillis().toString()
        eventTypeText.text = event.eventType
        eventDescText.text = event.description
        swipeView.alpha = 1f
    }

    @Click(R.id.cardSwipeView_Image)
    fun onClick() {
        Log.d("EVENT", "profileImageView click")
    }


    @SwipeCancelState
    fun onSwipeCancelState() {
        Log.d("DEBUG", "onSwipeCancelState")
        swipeView.alpha = 1f
    }


    @SwipeOutDirectional
    fun onSwipeOutDirectional(direction: SwipeDirection) {
        // Log.d("DEBUG", "SwipeOutDirectional " + direction.name)
        Log.d(Constants.TAG, "Swiped Left on Event ${event.title}")
        (context as MainActivity).onSwipeLeft(event)

    }

    @SwipeInDirectional
    fun onSwipeInDirectional(direction: SwipeDirection) {
        //Log.d("DEBUG", "SwipeInDirectional " + direction.name)
        Log.d(Constants.TAG, "Swiped Right on Event ${event.title}")
        (context as MainActivity).onSwipeRight(event)
    }

    @SwipingDirection
    fun onSwipingDirection(direction: SwipeDirection) {
        Log.d("DEBUG", "SwipingDirection " + direction.name)
    }

    @SwipeTouch
    fun onSwipeTouch(xStart: Float, yStart: Float, xCurrent: Float, yCurrent: Float) {

        val cardHolderDiagonalLength =
            sqrt(
                Math.pow(cardViewHolderSize.x.toDouble(), 2.0)
                        + (Math.pow(cardViewHolderSize.y.toDouble(), 2.0))
            )
        val distance = sqrt(
            Math.pow(xCurrent.toDouble() - xStart.toDouble(), 2.0)
                    + (Math.pow(yCurrent.toDouble() - yStart, 2.0))
        )

        val alpha = 1 - distance / cardHolderDiagonalLength

        Log.d(
            "DEBUG", "onSwipeTouch "
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


}