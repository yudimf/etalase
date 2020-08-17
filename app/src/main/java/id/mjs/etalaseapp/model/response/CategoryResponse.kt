package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName
import id.mjs.etalaseapp.model.Category

data class CategoryResponse(
    @SerializedName("code")
    var code: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var listCategory: ArrayList<Category>? = null
)