package id.mjs.etalaseapp.model

import android.os.Parcel
import android.os.Parcelable

class Download : Parcelable {
    constructor() {}

    var progress = 0
    var currentFileSize = 0
    var totalFileSize = 0
    var isComplete = false

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(progress)
        dest.writeInt(currentFileSize)
        dest.writeInt(totalFileSize)
    }

    private constructor(`in`: Parcel) {
        progress = `in`.readInt()
        currentFileSize = `in`.readInt()
        totalFileSize = `in`.readInt()
    }

    companion object {
        val CREATOR: Parcelable.Creator<Download?> = object : Parcelable.Creator<Download?> {
            override fun createFromParcel(`in`: Parcel): Download? {
                return Download(`in`)
            }

            override fun newArray(size: Int): Array<Download?> {
                return arrayOfNulls(size)
            }
        }
    }
}