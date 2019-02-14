package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.graphics.Picture
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

//TODO: Map event IDs to true/false values

@Parcelize
data class User(
    var authID: String ="",
    var userID: String = "",
    var fullName: String = "",
    var photoUrl: String = "",
    var age: Int = 0,
    var sex: String = "",
    var locationID: Int = 0,
    var description: String = "",
    var eventMap: MutableMap<String, Int> = mutableMapOf()
//    var eventsHosting: Map<String, Boolean>? = null,
//    var eventsAppliedTo: Map<String, Boolean>? = null,
//    var eventsAcceptedTo: Map<String, Boolean>? = null,
//    var eventsDeniedFrom: Map<String, Boolean>? = null
) : Parcelable {

//    //    @ServerTimestamp
////    var timestamp: Timestamp? = null
    @get: Exclude
    var id = ""
//
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readInt(),
//        parcel.readString(),
//        parcel.readInt(),
//        parcel.readString(),
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(userID)
//        parcel.writeString(fullName)
//        parcel.readString()
//        parcel.writeInt(age)
//        parcel.writeString(sex)
//        parcel.writeInt(locationID)
//        parcel.writeString(description)
//
//
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
    companion object  {

        fun fromSnapshot(snapshot: DocumentSnapshot): User {
            val user = snapshot.toObject(User::class.java)!!
            user.id = snapshot.id
            return user
        }
    }
}