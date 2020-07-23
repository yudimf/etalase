package id.mjs.etalaseapp.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import okhttp3.ResponseBody
import java.io.*
import kotlin.math.pow
import kotlin.math.roundToInt

class DownloadService : IntentService("Download Service") {
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private lateinit var updatedApk : File
    private var totalFileSize = 0

    override fun onHandleIntent(p0: Intent?) {
        Log.d("asupdieu","dieu")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Download")
            .setContentText("Downloading File")
            .setAutoCancel(true)
        notificationManager?.notify(0, notificationBuilder?.build())
        initDownload()
    }

    private fun initDownload(){
        val request = ApiMain().services.getSampleApps()
        try {
            downloadFile(request.execute().body())
        }catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadFile(body: ResponseBody?){

        val fileName = """${System.currentTimeMillis()}.apk"""
        var destination =
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += fileName

        val path = destination

        updatedApk = File(
            path
        )

        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        Log.d("filesize",fileSize.toString())
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val outputFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.apk")
        val output: OutputStream = FileOutputStream(updatedApk)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        while (bis.read(data).also { count = it } != -1) {
            total += count.toLong()
            totalFileSize = (fileSize / 1024.0.pow(2.0)).toInt()
            val current = (total / 1024.0.pow(2.0)).roundToInt().toDouble()
            val progress = (total * 100 / fileSize).toInt()
            val currentTime = System.currentTimeMillis() - startTime
            val download = Download()
            download.totalFileSize = totalFileSize
            if (currentTime > 1000 * timeCount) {
                Log.d("currentSize",current.toString())
                download.currentFileSize = current.toInt()
                download.progress = progress
                sendNotification(download)
                timeCount++
            }
            output.write(data, 0, count)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendNotification(download: Download) {
        sendIntent(download)
//                notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder!!.setContentText(String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize))
        notificationManager!!.notify(0, notificationBuilder!!.build())
    }

    private fun sendIntent(download: Download) {
        val intent = Intent(DownloadActivity.MESSAGE_PROGRESS)
        intent.putExtra("download", download)
        LocalBroadcastManager.getInstance(this@DownloadService).sendBroadcast(intent)
    }

    private fun onDownloadComplete() {
        val download = Download()
        download.progress = 100
        sendIntent(download)
        notificationManager!!.cancel(0)
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setContentText("File Downloaded")
        notificationManager!!.notify(0, notificationBuilder!!.build())

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
//                        for android 5.0
//                        Uri.fromFile(updatedApk),
            FileProvider.getUriForFile(applicationContext, applicationContext.packageName+".provider", updatedApk),
            "application/vnd.android.package-archive"
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION )
        startActivity(intent)
    }
}