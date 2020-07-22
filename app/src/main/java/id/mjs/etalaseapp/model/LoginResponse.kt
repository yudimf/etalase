package id.mjs.etalaseapp.model

import com.google.gson.annotations.SerializedName
import id.mjs.etalaseapp.model.LoginDataResponse

data class LoginResponse (
    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: LoginDataResponse? = null
)