package id.mjs.etalaseapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val categoryId : Int,
    val categoryName : String,
    val categoryImage : Int
) : Parcelable