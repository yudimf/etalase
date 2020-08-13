package id.mjs.etalaseapp.model.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest (

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("password_confirmation")
    var password_confirmation: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("website")
    var website: String? = null,

    @SerializedName("country")
    var country: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("role_id")
    var role_id: Int? = null,

    @SerializedName("sdk_version")
    var sdk_version: String? = null

)