package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class EndUsers (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("picture") var picture : String? = null
)