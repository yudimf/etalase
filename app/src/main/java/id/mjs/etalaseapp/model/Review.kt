package id.mjs.etalaseapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Review(
    var reviewImage : Int,
    var reviewName : String,
    var reviewRating : Int,
    var reviewDate : String,
    var reviewDetail : String
) : Parcelable