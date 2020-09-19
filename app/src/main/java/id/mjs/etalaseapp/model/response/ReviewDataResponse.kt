package id.mjs.etalaseapp.model.response

import com.google.gson.annotations.SerializedName

data class ReviewDataResponse(
    @SerializedName("avg_ratings") var avg_ratings : Float? = null,
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("type") var type : String? = null,
    @SerializedName("app_icon") var app_icon : String? = null,
    @SerializedName("category_id") var category_id : Int? = null,
    @SerializedName("eu_sdk_version") var eu_sdk_version : Int? = null,
    @SerializedName("package_name") var package_name : String? = null,
    @SerializedName("rate") var rate : Int? = null,
    @SerializedName("version") var version : String? = null,
    @SerializedName("file_size") var file_size : Int? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("updates_description") var updates_description : String? = null,
    @SerializedName("link") var link : String? = null,
    @SerializedName("apk_file") var apk_file : String? = null,
    @SerializedName("expansion_file") var expansion_file : String? = null,
    @SerializedName("media") var media : String? = null,
    @SerializedName("developer_id") var developer_id : Int? = null,
    @SerializedName("is_approve") var is_approve : Int? = null,
    @SerializedName("reject_reason") var reject_reason : String? = null,
    @SerializedName("is_active") var is_active : Int? = null,
    @SerializedName("is_partnership") var is_partnership : String? = null,
    @SerializedName("created_at") var created_at : String? = null,
    @SerializedName("created_by") var created_by : Int? = null,
    @SerializedName("updated_at") var updated_at : String? = null,
    @SerializedName("updated_by") var updated_by : Int? = null,
    @SerializedName("review") var review : List<Review>? = null
)