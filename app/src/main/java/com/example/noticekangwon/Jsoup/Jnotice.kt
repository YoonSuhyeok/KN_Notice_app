package com.example.noticekangwon.Jsoup

import android.os.Parcel
import android.os.Parcelable

data class Jnotice(val mIdFk: Int, val mTitle: String?, val mUrl: String?, val mDate: String?, val mExtension: Boolean): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mIdFk)
        parcel.writeString(mTitle)
        parcel.writeString(mUrl)
        parcel.writeString(mDate)
        parcel.writeByte(if (mExtension) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Jnotice> {
        override fun createFromParcel(parcel: Parcel): Jnotice {
            return Jnotice(parcel)
        }

        override fun newArray(size: Int): Array<Jnotice?> {
            return arrayOfNulls(size)
        }
    }
}
