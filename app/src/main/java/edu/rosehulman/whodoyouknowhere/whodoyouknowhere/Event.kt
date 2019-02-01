package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import java.util.*


data class Event(
    var eventID: Int = 0, var title: String="Title", var date: String="1/12/19", var location: String="Olympus Mons,Mars", var description: String = "A fun get-together!",
    var ageRestriction: Boolean= false, var eventType: String = "Party",
    var entryFee: Double = 0.0, var password: String = "",
    var hostID: Int = 0, var attendeeList: ArrayList<User> = ArrayList<User>(0)
) {

}