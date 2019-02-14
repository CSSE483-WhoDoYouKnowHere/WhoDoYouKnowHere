package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var eventID: String = "",
    var title: String = "Title",
    var date: String = "1/12/19",
    var location: String = "Olympus Mons,Mars",
    var picUrl: String = "",
    var description: String = "A fun get-together!",
    var ageRestriction: Boolean = false,
    var eventType: String = "Party",
    var entryFee: Double = 0.0,
    var password: String = "",
    var hostID: Int = 0,
    var userMap : MutableMap<String, Int> = mutableMapOf()
//    var applicantList: HashMap<String, Boolean>? = null,
//    var acceptedList: HashMap<String, Boolean>? = null,
//    var deniedList: HashMap<String, Boolean>? = null
    // var hashMap: Bundle = Bundle()

) : Parcelable {


    //    @ServerTimestamp
//    var timeStamp: Timestamp? = null
    @get:Exclude
    var id = ""

    //    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readByte() != 0.toByte(),
//        parcel.readString(),
//        parcel.readDouble(),
//        parcel.readString(),
//        parcel.readInt(),
//        parcel.createTypedArrayList(User.CREATOR),
//        parcel.createTypedArrayList(User.CREATOR),
//        parcel.createTypedArrayList(User.CREATOR)
//       // parcel.readBundle(ClassLoader.getSystemClassLoader())
//    )
//
    companion object {

        fun fromSnapshot(snapshot: DocumentSnapshot): Event {
            val event = snapshot.toObject(Event::class.java)!!
            event.id = snapshot.id
            return event

        }
    }
//
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(eventID)
//        parcel.writeString(title)
//        parcel.writeString(date)
//        parcel.writeString(location)
//        parcel.writeString(picUrl)
//        parcel.writeString(description)
//        parcel.writeByte(if (ageRestriction) 1 else 0)
//        parcel.writeString(eventType)
//        parcel.writeDouble(entryFee)
//        parcel.writeString(password)
//        parcel.writeInt(hostID)
//        parcel.writeTypedList(applicantList)
//        parcel.writeTypedList(acceptedList)
//        parcel.writeTypedList(deniedList)
//       // parcel.writeBundle(hashMap)
//    }

}