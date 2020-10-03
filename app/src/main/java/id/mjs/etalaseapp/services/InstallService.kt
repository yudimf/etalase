package id.mjs.etalaseapp.services

import android.app.IntentService
import android.content.Intent
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File


class InstallService : IntentService("InstallService") {

    companion object {
        const val EXTRA_FILE_PATH = "extra_file_path"
    }

    override fun onHandleIntent(intent: Intent?) {

        val path = intent?.getStringExtra(EXTRA_FILE_PATH)
        val file = File(path)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
//                        for android 5.0
//                        Uri.fromFile(updatedApk),
            FileProvider.getUriForFile(applicationContext, applicationContext.packageName+".provider", file),
            "application/vnd.android.package-archive"
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION )
        startActivity(intent)
    }

}
