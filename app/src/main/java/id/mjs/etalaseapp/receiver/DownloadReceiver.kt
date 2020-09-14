package id.mjs.etalaseapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.StatusDownloadedDataResponse
import id.mjs.etalaseapp.model.response.StatusDownloadedResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DownloadReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_REQUEST_CODE = "extra_request_code"
        const val EXTRA_JWT = "extra_jwt"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val requestCode = intent.extras?.getInt(EXTRA_REQUEST_CODE)
        val packageName = intent.extras?.getString(DownloadActivity.EXTRA_APP_MODEL)
        val jwt = intent.extras?.getString(EXTRA_JWT)
        Log.d("packageName",packageName.toString())
        val status = appInstalledOrNot(context,packageName.toString())
        Log.d("packageName",status.toString())
        if (status){
            ApiMain().services.postStatusDownload(jwt,requestCode).enqueue(object : Callback<StatusDownloadedResponse>{
                override fun onFailure(call: Call<StatusDownloadedResponse>, t: Throwable) {
                    Log.d("onFailure",t.message.toString())
                }

                override fun onResponse(call: Call<StatusDownloadedResponse>, response: Response<StatusDownloadedResponse>) {
                    if (response.isSuccessful){
                        Log.d("onResponse",response.message())
                    }
                }
            })
        }
        Toast.makeText(context,"This from $requestCode", Toast.LENGTH_SHORT).show()
    }

    private fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm: PackageManager = context.packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
}
