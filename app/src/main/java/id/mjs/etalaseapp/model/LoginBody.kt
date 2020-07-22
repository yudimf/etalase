package id.mjs.etalaseapp.model

import com.google.gson.annotations.SerializedName

data class LoginBody (
    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null
)