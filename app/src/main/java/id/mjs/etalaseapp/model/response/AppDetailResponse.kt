package id.mjs.etalaseapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppDetailResponse (
    @SerializedName("code") var code : Int? = null,
    @SerializedName("message") var message : String? = null,
    @SerializedName("data") var appDataResponse: AppDataResponse? = null
) : Parcelable