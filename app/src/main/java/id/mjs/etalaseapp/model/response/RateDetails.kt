package id.mjs.etalaseapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RateDetails (
    @SerializedName("1") var one : Int? = null,
    @SerializedName("2") var two : Int? = null,
    @SerializedName("3") var three : Int? = null,
    @SerializedName("4") var four : Int? = null,
    @SerializedName("5") var five : Int? = null
) : Parcelable