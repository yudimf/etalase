package id.mjs.etalaseapp.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Developers (
    @SerializedName("name") var name : String? = null,
    @SerializedName("email") var email : String?  = null,
    @SerializedName("picture") var picture : String? = null,
    @SerializedName("dev_web") var dev_web : String? = null
) : Parcelable