package edu.rosehulman.whodoyouknowhere.whodoyouknowhere


data class Event(
    val eventID: Int, var title: String, var description: String,
    var ageRestriction: Boolean, var eventType: String,
    var entryFee: Double, var password: String,
    val hostID: Int, var attendeeList: ArrayList<User>
) {

}