package id.mjs.etalaseapp.ui.download

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.services.DownloadService
import kotlinx.android.synthetic.main.activity_download.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val fileName = """${System.currentTimeMillis()}.apk"""
        var destination =
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += fileName

        btn_download.setOnClickListener {
            val intent = Intent(this, DownloadService::class.java)
            startService(intent)
            btn_download.visibility = View.INVISIBLE
            progress.visibility = View.VISIBLE
            progress_text.visibility = View.VISIBLE
            progress.isIndeterminate = true
            Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
        }

//        btn_download.setOnClickListener {
//            Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
//            ApiMain().services.getSampleApps().enqueue(object : Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//                }
//
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    val path = destination
//                    response.body()?.byteStream()?.saveToFile( path)
//                    Log.d("asup",path)
//                    val updatedApk = File(
//                        path
//                    )
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.setDataAndType(
////                        for android 5.0
////                        Uri.fromFile(updatedApk),
//                        FileProvider.getUriForFile(applicationContext, applicationContext.packageName+".provider", updatedApk),
//                        "application/vnd.android.package-archive"
//                    )
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
//                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION )
////                    startActivity(intent)
//                }
//            })
//        }

        imageViewDownload.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://play.google.com/store/apps/details?id=com.androbaby.game2048")
            startActivity(openURL)
        }

        registerReceiver()
    }

    private fun InputStream.saveToFile(file: String) = use { input ->
        File(file).outputStream().use { output ->
            input.copyTo(output)
        }
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(MESSAGE_PROGRESS)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == MESSAGE_PROGRESS) {
                val download: Download = intent.getParcelableExtra("download")
                progress!!.progress = download.progress
                if (download.progress == 100) {
                    progress_text!!.text = "File Download Complete"
                } else {
                    progress.isIndeterminate = false
                    progress_text!!.text = String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize)
                }
            }
        }
    }

    companion object {
        const val MESSAGE_PROGRESS = "message_progress"
        private const val PERMISSION_REQUEST_CODE = 1
    }
}