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
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CardViewAdapter
import id.mjs.etalaseapp.adapter.ReviewAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.Review
import id.mjs.etalaseapp.services.DownloadService
import id.mjs.etalaseapp.ui.detail.DetailActivity
import id.mjs.etalaseapp.ui.review.ReviewActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.item_app_cardview.view.*
import java.io.File
import java.io.InputStream

class DownloadActivity : AppCompatActivity() {

    lateinit var appModelSelected : AppModel

    private var list = ArrayList<AppModel>()
    private var listReview = ArrayList<Review>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        appModelSelected = intent.getParcelableExtra(EXTRA_APP_MODEL) as AppModel
        initLayout()
        registerReceiver()
    }

    private fun initSimilarAppsLayout(){
        addList()
        val cardViewAdapter = CardViewAdapter(list)
        rv_list_apps_sejenis.setHasFixedSize(true)
        rv_list_apps_sejenis.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps_sejenis.adapter = cardViewAdapter
    }

    private fun initReviewLayout(){
        listReview.add(Review(1,"1",1,"1","1"))
        listReview.add(Review(1,"1",1,"1","1"))
        val reviewAdapter = ReviewAdapter(listReview)
        rv_review.setHasFixedSize(true)
        rv_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_review.adapter = reviewAdapter
    }

    private fun initDownloadButton(){
        btn_download_1.setOnClickListener {
            if (appModelSelected.is_embeded_app){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(appModelSelected.playStoreLink)
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
    }

    private fun initLayout(){
        initSimilarAppsLayout()
        initReviewLayout()
        initDownloadButton()

//        image_view_download_1.setImageResource(appModelSelected.photo)
        val picasso = Picasso.get()
        picasso.load(Utils.baseUrl+"apps/"+appModelSelected.photoPath)
            .into(image_view_download_1)
        app_name_download_1.text = appModelSelected.name
        val fileSize = appModelSelected.file_size.toString() + "  MB"
        file_size_download_1.text = fileSize

//        image_view_download_1.setOnClickListener{
//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data = Uri.parse(appModelSelected.playStoreLink)
//            startActivity(openURL)
//        }

        detail_app_btn.setOnClickListener {
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_APP_MODEL,appModelSelected)
            startActivity(intent)
        }

        all_review_btn.setOnClickListener {
            val intent = Intent(applicationContext, ReviewActivity::class.java)
            startActivity(intent)
        }

        btn_back_download.setOnClickListener {
            finish()
        }
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
        intent.putExtra(DownloadService.EXTRA_APP_MODEL,appModelSelected)
        startService(intent)
        btn_download_1.visibility = View.INVISIBLE
        progress_bar_download.visibility = View.VISIBLE
        progress_text_download.visibility = View.VISIBLE
        progress_bar_download.isIndeterminate = true
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
                progress_bar_download!!.progress = download.progress
                if (download.progress == 100) {
                    progress_text_download!!.text = "File Download Complete"
                } else {
                    progress_bar_download.isIndeterminate = false
                    progress_text_download!!.text = String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize)
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
        list.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd",getString(R.string.desc_tokopedia),true,44,""))
        list.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps",getString(R.string.desc_alfacart),true,17,""))
        list.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift",getString(R.string.desc_alfagift),true,18,""))
        list.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce",getString(R.string.desc_blibli),true,26,""))
        list.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari",getString(R.string.desc_matahari_dept),true,21,""))
        list.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe",getString(R.string.desc_babe),true,34,""))
        list.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca",getString(R.string.desc_baca),true,20,""))
        list.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss",getString(R.string.desc_detik),true,18,""))
        list.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone",getString(R.string.desc_cnn),true,14,""))
        list.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz",getString(R.string.desc_catfiz),true,14,""))
        list.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android",getString(R.string.desc_vidio),true,13,""))
        list.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63,""))
    }
}