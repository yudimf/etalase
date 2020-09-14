package id.mjs.etalaseapp.model.request

import com.google.gson.annotations.SerializedName

data class UpdateDataRequest (
    @SerializedName("package_name") var package_name : String? = null,
    @SerializedName("version") var version : String? = null
)