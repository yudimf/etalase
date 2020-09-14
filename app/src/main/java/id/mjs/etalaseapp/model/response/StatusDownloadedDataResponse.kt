package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class StatusDownloadedDataResponse (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("apps_id") var apps_id : Int ? = null,
    @SerializedName("end_users_id") var end_users_id : Int? = null,
    @SerializedName("clicked") var clicked : String? = null,
    @SerializedName("installed") var installed : String? = null,
    @SerializedName("version") var version : Int? = null,
    @SerializedName("download_at") var download_at : String? = null
)