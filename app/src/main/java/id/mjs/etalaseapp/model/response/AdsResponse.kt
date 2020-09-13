package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class AdsResponse (
    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: ArrayList<AdsDataResponse>? = null
)