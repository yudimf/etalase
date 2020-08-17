package id.mjs.etalaseapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("id")
    val categoryId : Int,

    @SerializedName("name")
    val categoryName : String,

    @SerializedName("icon")
    val categoryImage : String
) : Parcelable