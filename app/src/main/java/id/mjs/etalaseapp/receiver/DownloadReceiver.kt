package id.mjs.etalaseapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity


class DownloadReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_REQUEST_CODE = "extra_request_code"
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val requestCode = intent.extras?.getInt(EXTRA_REQUEST_CODE)
        val packageName = intent.extras?.getString(DownloadActivity.EXTRA_APP_MODEL)
        Log.d("packageName",packageName.toString())
        val status = appInstalledOrNot(context,packageName.toString())
        Log.d("packageName",status.toString())
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
