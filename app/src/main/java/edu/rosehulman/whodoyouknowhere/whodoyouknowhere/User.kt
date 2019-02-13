package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.graphics.Picture
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class User(
    val userID: String = "",
    var fullName: String = "",
    var age: Int = 0,
    var sex: String = "",
    var locationID: Int = 0,
    var description: String = "",
    var eventsHosting: ArrayList<Event> = arrayListOf(Event()),
    var eventsAppliedTo: ArrayList<Event> = arrayListOf(Event()),
    var eventsAcceptedTo: ArrayList<Event> = arrayListOf(Event()),
    var eventsDeniedFrom: ArrayList<Event> = arrayListOf(Event())
) : Parcelable {

//    @ServerTimestamp
//    var timestamp: Timestamp? = null
    @get: Exclude
    var id = ""


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(Event.CREATOR),
        parcel.createTypedArrayList(Event.CREATOR),
        parcel.createTypedArrayList(Event.CREATOR),
        parcel.createTypedArrayList(Event.CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userID)
        parcel.writeString(fullName)
        parcel.writeInt(age)
        parcel.writeString(sex)
        parcel.writeInt(locationID)
        parcel.writeString(description)
        parcel.writeTypedList(eventsHosting)
        parcel.writeTypedList(eventsAppliedTo)
        parcel.writeTypedList(eventsAcceptedTo)
        parcel.writeTypedList(eventsDeniedFrom)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        fun fromSnapshot(snapshot: DocumentSnapshot): User {
            val user = snapshot.toObject(User::class.java)!!
            user.id = snapshot.id
            return user
        }
    }


}