package id.mjs.etalaseapp.ui.download

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        btn_download.setOnClickListener {
            if (appModelSelected.is_embeded_app){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(appModelSelected.playstoreLink)
                startActivity(openURL)
            }
            else{
                if (checkPermission()) {
                    startDownload()
                } else {
                    requestPermission()
                }
            }
        }

        imageViewDownload.setImageResource(appModelSelected.photo)
        textView4.text = appModelSelected.name
        tv_tentang_app.text = appModelSelected.description
        val fileSize = appModelSelected.file_size.toString() + "  MB"
        download_file_size.text = fileSize

        imageViewDownload.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(appModelSelected.playstoreLink)
            startActivity(openURL)
        }

        registerReceiver()
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }

    private fun startDownload(){
        val intent = Intent(this, DownloadService::class.java)
        startService(intent)
        btn_download.visibility = View.INVISIBLE
        progress.visibility = View.VISIBLE
        progress_text.visibility = View.VISIBLE
        progress.isIndeterminate = true
        Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
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
        list.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd",getString(R.string.desc_tokopedia),true,44))
        list.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps",getString(R.string.desc_alfacart),true,17))
        list.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift",getString(R.string.desc_alfagift),true,18))
        list.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce",getString(R.string.desc_blibli),true,26))
        list.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari",getString(R.string.desc_matahari_dept),true,21))
        list.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe",getString(R.string.desc_babe),true,34))
        list.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca",getString(R.string.desc_baca),true,20))
        list.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss",getString(R.string.desc_detik),true,18))
        list.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone",getString(R.string.desc_cnn),true,14))
        list.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz",getString(R.string.desc_catfiz),true,14))
        list.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android",getString(R.string.desc_vidio),true,13))
        list.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63))
    }
}