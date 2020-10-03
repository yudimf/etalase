package id.mjs.etalaseapp.utils

import id.mjs.etalaseapp.adapter.MediaAdapter
import kotlin.math.pow

object Utils {
//    const val baseUrl : String = "http://api.etalase.web.id/"
    const val baseUrl : String = "https://api-etalase-app.bagustech.id/"
    const val signature : String = "hfauef874h2bfjb2ufh2b"

    const val ITEM_VIDEO = 1
    const val ITEM_IMAGE = 2

    fun convertBiteToMB(size : Int) : Int{
        return (size / 1024.0.pow(2.0)).toInt()
    }

    fun getExtension(url: String) : String{
        val fileName: String = url

        val index = fileName.lastIndexOf('.')
        return when {
            index > 0 -> {
                fileName.substring(index + 1)
            }
            else -> {
                ""
            }
        }
    }

    fun getItemType(url: String) : Int{
        return if (getExtension(url) == "mp4" || getExtension(url) == "MP4" ||
                    getExtension(url) == "m4A" || getExtension(url) == "M4A" ||
                    getExtension(url) == "mkv" || getExtension(url) == "MKV" ||
                    getExtension(url) == "wav" || getExtension(url) == "WAV" ||
                    getExtension(url) == "flv" || getExtension(url) == "FLV"){
            ITEM_VIDEO
        } else{
            ITEM_IMAGE
        }
    }
}