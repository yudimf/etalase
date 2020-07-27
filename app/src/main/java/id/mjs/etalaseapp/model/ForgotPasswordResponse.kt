package id.mjs.etalaseapp.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse (
    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: String? = null

)