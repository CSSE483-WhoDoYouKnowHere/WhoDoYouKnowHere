package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import java.util.*


data class Event(
    val eventID: Int = 0, var title: String, var date: Date, var location: String, var description: String = "A fun get-together!",
    var ageRestriction: Boolean, var eventType: String = "Party",
    var entryFee: Double = 0.00, var password: String = "",
    val hostID: Int = 0, var attendeeList: ArrayList<User> = ArrayList<User>(0)
) {

}