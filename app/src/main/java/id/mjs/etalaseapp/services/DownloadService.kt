package id.mjs.etalaseapp.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.StatusDownloadedResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import kotlin.math.pow
import kotlin.math.roundToInt


class DownloadService : IntentService("Download Service") {
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private lateinit var updatedApk : File
    private var totalFileSize = 0

    lateinit var appModelSelected : AppDataResponse

    companion object {
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    override fun onHandleIntent(p0: Intent?) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        appModelSelected = p0?.getParcelableExtra<AppDataResponse>(DownloadActivity.EXTRA_APP_MODEL) as AppDataResponse

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()

        Log.d("appmodelselected",appModelSelected.name.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_01"
            val name: CharSequence = "my_channel"
            val description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = description
            notificationManager!!.createNotificationChannel(mChannel)
        }

        notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle("Download")
            .setContentText("Downloading File")
            .setAutoCancel(true)

//        notificationManager?.notify(0, notificationBuilder?.build())

        initDownload()
    }

    private fun initDownload(){
//        val request = ApiMain().services.getSampleApps()
        val request = ApiMain().services.getApp(appModelSelected.apk_file.toString())
        try {
            downloadFile(request.execute().body())
        }catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadFile(body: ResponseBody?){

        val fileName = """${System.currentTimeMillis()}.apk"""
        var destination = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += fileName

        val path = destination

        updatedApk = File(
            path
        )

        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val output: OutputStream = FileOutputStream(updatedApk)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        while (bis.read(data).also { count = it } != -1) {
            Log.d("asupdieulah","ya")
            total += count.toLong()
            totalFileSize = (fileSize / 1024.0.pow(2.0)).toInt()
            val current = (total / 1024.0.pow(2.0)).roundToInt().toDouble()
            val progress = (total * 100 / fileSize).toInt()
            val currentTime = System.currentTimeMillis() - startTime
            val download = Download()
            download.totalFileSize = totalFileSize
//            if (currentTime > 1000 * timeCount) {
                Log.d("currentSize",current.toString())
                download.currentFileSize = current.toInt()
                download.progress = progress
                sendNotification(download)
                timeCount++
//            }
            output.write(data, 0, count)
        }
        onDownloadComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun sendNotification(download: Download) {
        Log.d("sendNotification",download.toString())
        sendIntent(download)
//        notificationBuilder!!.setProgress(100,download.progress,false)
//        notificationBuilder!!.setContentText(String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize))
//        notificationManager!!.notify(0, notificationBuilder!!.build())
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
//        notificationManager!!.cancel(0)
//        notificationBuilder!!.setProgress(0, 0, false)
//        notificationBuilder!!.setContentText("File Downloaded")
//        notificationManager!!.notify(0, notificationBuilder!!.build())

        Log.d("installing","masuk")

        ApiMain().services.postStatusDownload(jwt,appModelSelected.idApps).enqueue(object :
            Callback<StatusDownloadedResponse> {
            override fun onFailure(call: Call<StatusDownloadedResponse>, t: Throwable) {
                Log.d("failpostdownload","failpostdownload")
            }

            override fun onResponse(call: Call<StatusDownloadedResponse>, response: Response<StatusDownloadedResponse>) {
                if (response.isSuccessful){
                    val it = response.body()
                    Log.d("statusPostDownload",it?.message.toString())
                    Toast.makeText(application,it?.message.toString(),Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.d("failpostdownload","failpostdownload")
                }
            }

        })

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