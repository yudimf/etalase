package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class DataUserInfoResponse (
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("picture")
    var picture: String? = null,

    @SerializedName("eu_birthday")
    var birthDate: String? = null

)