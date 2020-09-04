package id.mjs.etalaseapp.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest (
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("sdk_version")
    var sdk_version: String? = null,

    @SerializedName("imei_1")
    var imei1: String? = null,

    @SerializedName("imei_2")
    var imei2: String? = null,

    @SerializedName("device_brand")
    var deviceBrand: String? = null,

    @SerializedName("device_model")
    var deviceModel: String? = null

): Parcelable