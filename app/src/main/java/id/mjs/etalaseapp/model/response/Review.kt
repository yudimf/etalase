package id.mjs.etalaseapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("apps_id") var apps_id : Int? = null,
    @SerializedName("end_users_id") var end_users_id : Int? = null,
    @SerializedName("ratings") var ratings : Int? = null,
    @SerializedName("comment") var comment : String? = null,
    @SerializedName("users_dev_id") var users_dev_id : Int? = null,
    @SerializedName("reply") var reply : String? = null,
    @SerializedName("comment_at") var comment_at : String? = null,
    @SerializedName("reply_at") var reply_at : String? = null,
    @SerializedName("read_at") var read_at : String? = null,
    @SerializedName("created_at") var created_at : String? = null,
    @SerializedName("updated_at") var updated_at : String? = null,
    @SerializedName("endusers") var endusers : EndUsers? = null
) : Parcelable