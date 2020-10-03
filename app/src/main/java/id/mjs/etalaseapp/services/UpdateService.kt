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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.StatusDownloadedResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.myapps.downloadedapps.DownloadedAppsFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import kotlin.math.pow
import kotlin.math.roundToInt

class UpdateService : IntentService("UpdateService") {

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private lateinit var fileDownloaded : File
    private lateinit var expansionFileDownloaded : File
    private var totalFileSize = 0
    private var totalExpansionFileSize = 0

    private var isUpdateFinish = false

    private var waitingIntentCount = 0

    lateinit var appModelSelected : AppDataResponse

    companion object {
        const val EXTRA_APP_MODEL = "extra_app_model"
        const val valCHANNEL_ID = "my_channel_01"
        const val notifyID = 1
    }

    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        waitingIntentCount++
        Log.d("waitingIntentCount+",waitingIntentCount.toString())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        waitingIntentCount--
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        appModelSelected = intent?.getParcelableExtra<AppDataResponse>(EXTRA_APP_MODEL) as AppDataResponse

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_01"
            val name: CharSequence = "my_channel"
            val description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(valCHANNEL_ID, name, importance)
            mChannel.description = description
            notificationManager!!.createNotificationChannel(mChannel)
        }

        notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle(appModelSelected.name)
            .setContentText("Downloading File")
            .setAutoCancel(true)
            .setChannelId(valCHANNEL_ID)

        notificationManager?.notify(appModelSelected.idApps!!, notificationBuilder?.build())

        initDownload(appModelSelected.apk_file.toString())
    }

    private fun initDownload(url : String){
        val request : Call<ResponseBody> = ApiMain().services.getApp(url)

        try {
            downloadFile(request.execute().body())
        }catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadFile(body: ResponseBody?){
        var fileName : String = ""
        var destination : String = ""

        fileName = appModelSelected.package_name.toString()
        destination = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/update/"
        val dir = File(destination)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        destination += fileName

        val path = destination

        fileDownloaded = File(
            path
        )

        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val output: OutputStream = FileOutputStream(fileDownloaded)
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
        sendIntent(download)
        Log.d("sendNotification",download.progress.toString())
        if (download.progress < 100){
            notificationBuilder!!.setProgress(100,download.progress,false)
            notificationBuilder!!.setContentText("Downloading "+appModelSelected.name+ " "+download.currentFileSize+"/"+download.totalFileSize+"MB")
            notificationManager!!.notify(appModelSelected.idApps!!, notificationBuilder!!.build())
        }
        else{
            notificationBuilder!!.setProgress(100,download.progress,false)
            notificationBuilder!!.setContentText("Download Complete")
            notificationManager!!.notify(appModelSelected.idApps!!, notificationBuilder!!.build())
        }
    }

    private fun sendIntent(download: Download) {
        val intent = Intent(DownloadedAppsFragment.MESSAGE_PROGRESS)
        intent.putExtra("download", download)
        intent.putExtra("appName", appModelSelected.name)
        intent.putExtra("isUpdateFinish", isUpdateFinish)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun onDownloadComplete() {
        val download = Download()
        download.progress = 100
        sendIntent(download)
        Log.d("waitingIntentCount",waitingIntentCount.toString())
        notificationBuilder!!.setProgress(100,download.progress,false)
        notificationBuilder!!.setContentText("Download Complete")
        notificationManager!!.notify(appModelSelected.idApps!!, notificationBuilder!!.build())

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

        if (appModelSelected.expansion_file != null ){
            initDownloadExpansion(appModelSelected.expansion_file!!)
        }
        else{
            if (waitingIntentCount == 0){
                Log.d("waitingIntentCount","Update Selesai")
                isUpdateFinish = true
                sendIntent(download)
                installUpdatedApps()
            }
        }

    }

    private fun installUpdatedApps(){

        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/update/"
        Log.d("installUpdatedApps", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
        Log.d("installUpdatedApps", "Size: "+ files.size)
        for (element in files) {
            Log.d("installUpdatedApps", "FileName:" + path+element.name)
            val intent = Intent(this, InstallService::class.java)
            intent.putExtra(InstallService.EXTRA_FILE_PATH,path+element.name)
            startService(intent)
        }
    }

    private fun initDownloadExpansion(url : String){
        val request : Call<ResponseBody> = ApiMain().services.getExtensionFile(url)

        try {
            downloadExpansionFile(request.execute().body())
        }catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun downloadExpansionFile(body: ResponseBody?){
        var fileName : String = ""
        var destination : String = ""

        fileName = appModelSelected.expansion_file.toString()
        destination = Environment.getExternalStorageDirectory().absolutePath+"/Android/obb/"+appModelSelected.package_name+"/"
        val dir = File(destination)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        destination += fileName

        val path = destination

        expansionFileDownloaded = File(
            path
        )

        var count: Int
        val data = ByteArray(1024 * 4)
        val fileSize = body!!.contentLength()
        val bis: InputStream = BufferedInputStream(body.byteStream(), 1024 * 8)
        val output: OutputStream = FileOutputStream(expansionFileDownloaded)
        var total: Long = 0
        val startTime = System.currentTimeMillis()
        var timeCount = 1
        while (bis.read(data).also { count = it } != -1) {
            Log.d("asupdieulah","ya")
            total += count.toLong()
            totalExpansionFileSize = (fileSize / 1024.0.pow(2.0)).toInt()
            val current = (total / 1024.0.pow(2.0)).roundToInt().toDouble()
            val progress = (total * 100 / fileSize).toInt()
            val currentTime = System.currentTimeMillis() - startTime
            val download = Download()
            download.totalFileSize = totalExpansionFileSize
//            if (currentTime > 1000 * timeCount) {
            Log.d("currentSize",current.toString())
            download.currentFileSize = current.toInt()
            download.progress = progress
            sendNotification(download)
            timeCount++
//            }
            output.write(data, 0, count)
        }
        onDownloadExpansionComplete()
        output.flush()
        output.close()
        bis.close()
    }

    private fun onDownloadExpansionComplete() {
        val download = Download()
        download.progress = 100
        sendIntent(download)
        notificationBuilder!!.setProgress(100,download.progress,false)
        notificationBuilder!!.setContentText("Download Complete")
        notificationManager!!.notify(appModelSelected.idApps!!, notificationBuilder!!.build())

        if (waitingIntentCount == 0){
            Log.d("waitingIntentCount","Update Selesai")
            isUpdateFinish = true
            sendIntent(download)
            installUpdatedApps()
        }
    }
}
