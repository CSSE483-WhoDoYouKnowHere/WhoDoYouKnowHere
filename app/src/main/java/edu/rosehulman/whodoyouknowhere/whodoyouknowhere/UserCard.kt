package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mindorks.placeholderview.SwipeDirection
import com.mindorks.placeholderview.annotations.*
import com.mindorks.placeholderview.annotations.swipe.*
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.sqrt

@Layout(R.layout.card_user_view)
class UserCard(
    private val attendeeListFragment: AttendeeListFragment,
    private val user: User,
    private val cardViewHolderSize: Point
) {

    @View(R.id.user_image)
    lateinit var profileImageView: ImageView

    @View(R.id.user_name)
    lateinit var name: TextView

    @View(R.id.user_age)
    lateinit var age: TextView

    @SwipeView
    lateinit var swipeView: android.view.View

    @JvmField
    @Position
    var position: Int = 0

    @Resolve
    fun onResolved() {

//        TODO: Give Users pictures :-)
//
//        Glide.with(context).load(user.picture).bitmapTransform(
//            RoundedCornersTransformation(
//                context,
//                Utils.dpToPx(7), //7
//                0,
//                RoundedCornersTransformation.CornerType.TOP)
//        )
//            .into(profileImageView)
        name.text = user.fullName
        age.text = user.age.toString()

        swipeView.alpha = 1f
    }

    @Click(R.id.user_image)
    fun onClick() {
        Log.d(Constants.TAG, "User clicked")
    }

    @SwipeCancelState
    fun onSwipeCancelState() {
        Log.d(Constants.TAG, "on Swipe Cancel State")
        swipeView.alpha = 1f
    }


    @SwipeOutDirectional
    fun onSwipeOutDirectional(direction: SwipeDirection) {
        // Log.d(Constants.TAG, "Swipe Out Directional " + direction.name)
        Log.d(Constants.TAG, "Swiped Left on User ${user.fullName}")
        attendeeListFragment.onSwipeLeft(user)

    }

    @SwipeInDirectional
    fun onSwipeInDirectional(direction: SwipeDirection) {
        //  Log.d(Constants.TAG, "Swipe In Directional " + direction.name)
        Log.d(Constants.TAG, "Swiped Right on User ${user.fullName}")
        attendeeListFragment.onSwipeRight(user)
    }

    @SwipingDirection
    fun onSwipingDirection(direction: SwipeDirection) {
        Log.d("DEBUG", "Swiping Direction " + direction.name)
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

        swipeView.alpha = alpha.toFloat()
    }

}