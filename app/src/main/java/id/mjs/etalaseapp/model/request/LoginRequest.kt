package id.mjs.etalaseapp.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("sdk_version")
    var sdk_version: String? = null
)