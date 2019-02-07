package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class Event(
    var eventID: Int = 0, var title: String="Title", var date: String="1/12/19", var location: String="Olympus Mons,Mars", var description: String = "A fun get-together!",
    var ageRestriction: Boolean= false, var eventType: String = "Party",
    var entryFee: Double = 0.0, var password: String = "",
    var hostID: Int = 0, var attendeeList: ArrayList<User> = ArrayList(0)
) : Parcelable {
    @ServerTimestamp
    var timeStamp: Timestamp? = null
    @get:Exclude
    var id = ""

    companion object CREATOR : Parcelable.Creator<Event> {

        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: DocumentSnapshot) : Event {
            val event = snapshot.toObject(Event:: class.java)!!
            //TODO
            event.id = snapshot.id
            return event
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(eventID)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

}