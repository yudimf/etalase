package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse (
    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: String? = null

)