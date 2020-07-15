package id.mjs.etalaseapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppModel (
    var photo: String,
    var name: String,
    var description: String
) : Parcelable