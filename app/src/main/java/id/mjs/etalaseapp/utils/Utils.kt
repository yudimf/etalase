package id.mjs.etalaseapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Utils {
    const val baseUrl : String = "http://api.etalase.web.id/"
//    const val baseUrl : String = "https://api-etalase-app.bagustech.id/"
    const val signature : String = "hfauef874h2bfjb2ufh2b"

    fun convertBiteToMB(size : Int) : Int{
        return size / 1024 / 1024
    }
}