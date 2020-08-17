package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse (
    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: DataUserInfoResponse? = null
)