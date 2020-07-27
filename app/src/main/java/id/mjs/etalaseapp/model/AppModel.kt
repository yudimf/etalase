package id.mjs.etalaseapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AppModel (
    var categoryId : Int,
    var photo: Int,
    var name: String,
    var downloadLink : String,
    var playstoreLink: String,
    var description: String,
    var is_embeded_app: Boolean,
    var file_size: Int
) : Parcelable