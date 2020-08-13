package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class LoginDataResponse (
    @SerializedName("user_id")
    var user_id: Int? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("role_id")
    var role_id: String? = null,

    @SerializedName("token")
    var token: String? = null
)