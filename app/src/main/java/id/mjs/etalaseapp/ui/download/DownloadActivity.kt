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
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CardViewAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.services.DownloadService
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class DownloadActivity : AppCompatActivity() {

    lateinit var appModelSelected : AppModel

    private var list = ArrayList<AppModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        appModelSelected = intent.getParcelableExtra(EXTRA_APP_MODEL) as AppModel

        addList()
        val cardViewAdapter = CardViewAdapter(list)
        rv_list_apps_sejenis.setHasFixedSize(true)
        rv_list_apps_sejenis.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps_sejenis.adapter = cardViewAdapter

//        val fileName = """${System.currentTimeMillis()}.apk"""
//        var destination =
//            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
//        destination += fileName

        btn_download.setOnClickListener {
            if (appModelSelected.is_embeded_app){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(appModelSelected.playstoreLink)
                startActivity(openURL)
            }
            else{
                val intent = Intent(this, DownloadService::class.java)
                startService(intent)
                btn_download.visibility = View.INVISIBLE
                progress.visibility = View.VISIBLE
                progress_text.visibility = View.VISIBLE
                progress.isIndeterminate = true
                Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
            }
        }

        imageViewDownload.setImageResource(appModelSelected.photo)
        textView4.text = appModelSelected.name

        imageViewDownload.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(appModelSelected.playstoreLink)
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
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    private fun addList(){
        for (i in 0 until 9){
            list.add(AppModel(1,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=com.androbaby.game2048","",false))
        }
    }
}