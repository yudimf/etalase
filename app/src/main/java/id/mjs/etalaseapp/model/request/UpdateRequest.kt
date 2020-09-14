package id.mjs.etalaseapp.model.request

import com.google.gson.annotations.SerializedName

data class UpdateRequest (
    @SerializedName("data") var data : ArrayList<UpdateDataRequest>? = null
)
