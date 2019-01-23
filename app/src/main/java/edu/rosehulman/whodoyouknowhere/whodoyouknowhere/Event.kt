package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.icu.util.CurrencyAmount
import com.google.firebase.firestore.auth.User
import javax.security.auth.callback.PasswordCallback

data class Event(val eventID: Int,var title:String, var description: String,
                 var ageRestriction: Boolean, var eventType: String,
                 var entryFee: CurrencyAmount,var password: PasswordCallback,
                 val hostID :Int,var attendeeList: ArrayList<User>) {

}