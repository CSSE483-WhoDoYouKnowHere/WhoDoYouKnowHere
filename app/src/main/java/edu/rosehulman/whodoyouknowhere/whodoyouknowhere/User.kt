package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.os.Parcel
import android.os.Parcelable

data class User(
    val userID: Int=  0, var fullName: String= "", var age: Int= 0, var gender: String ="",
    var privacy: Boolean = false, var locationID: Int =0, var description: String= "",
    var eventsHosting: ArrayList<Event> = arrayListOf(Event())
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.createTypedArrayList(Event.CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userID)
        parcel.writeString(fullName)
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeByte(if (privacy) 1 else 0)
        parcel.writeInt(locationID)
        parcel.writeString(description)
        parcel.writeTypedList(eventsHosting)
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
    }


}