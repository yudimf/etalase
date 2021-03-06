package id.mjs.etalaseapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class AppDataResponse (

    @SerializedName("id")
    var idApps: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("app_icon")
    var app_icon: String? = null,

    @SerializedName("category_id")
    var category_id: String? = null,

    @SerializedName("eu_sdk_version")
    var eu_sdk_version: String? = null,

    @SerializedName("package_name")
    var package_name: String? = null,

    @SerializedName("rate")
    var rate: String? = null,

    @SerializedName("version")
    var version: String? = null,

    @SerializedName("file_size")
    var file_size: Int? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("updates_description")
    var updates_description: String? = null,

    @SerializedName("link")
    var link: String? = null,

    @SerializedName("apk_file")
    var apk_file: String? = null,

    @SerializedName("expansion_file")
    var expansion_file: String? = null,

    @SerializedName("media")
    var media: ArrayList<String>? = null,

    @SerializedName("developer_id")
    var developer_id: String? = null,

    @SerializedName("is_active")
    var is_active: String? = null,

    @SerializedName("is_partnership")
    var is_partnership: String? = null,

    @SerializedName("apps_status")
    var apps_status: String? = null,

    @SerializedName("developers")
    var developers: @RawValue Developers? = null,

    @SerializedName("rate_details")
    var rateDetails: @RawValue RateDetails? = null,

    @SerializedName("avg_ratings")
    var avg_ratings : Double? = null,

    @SerializedName("review")
    var review : ArrayList<Review>? = null

) : Parcelable