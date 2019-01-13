package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.os.Parcel
import android.os.Parcelable
import com.google.type.Date

data class User(val userID: Int, var fullName: String, val dataOfBirth: Date, val gender: String, var privacy : Boolean, var locationID: Int, var description: String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        TODO("dataOfBirth"),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userID)
        parcel.writeString(fullName)
        parcel.writeString(gender)
        parcel.writeByte(if (privacy) 1 else 0)
        parcel.writeInt(locationID)
        parcel.writeString(description)
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