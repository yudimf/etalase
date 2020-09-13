package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class AdsDataResponse (

    @SerializedName("id")
    var id : Int? = null,

    @SerializedName("picture")
    var picture : String? = null,

    @SerializedName("name")
    var name : String? = null,

    @SerializedName("link")
    var link : String?= null,

    @SerializedName("orders")
    var orders : Int?= null,

    @SerializedName("created_at")
    var created_at : String?= null,

    @SerializedName("created_by")
    var created_by : Int?= null,

    @SerializedName("status")
    var status : Int?= null,

    @SerializedName("updated_by")
    var updated_by : String?= null,

    @SerializedName("updated_at")
    var updated_at : String?= null

)