package id.mjs.etalaseapp.ui.download

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.HomeCardViewAdapter
import id.mjs.etalaseapp.adapter.ReviewAdapter
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.Review
import id.mjs.etalaseapp.receiver.DownloadReceiver
import id.mjs.etalaseapp.services.DownloadService
import id.mjs.etalaseapp.ui.detail.DetailActivity
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.review.ReviewActivity
import id.mjs.etalaseapp.ui.searchapp.SearchAppActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.input_review_dialog.view.*
import java.io.File
import java.io.InputStream

class DownloadActivity : AppCompatActivity() {

    companion object {
        const val MESSAGE_PROGRESS = "message_progress"
        private const val PERMISSION_REQUEST_CODE = 1
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    private var mAlarmManager : AlarmManager? = null

    private lateinit var appModelSelected : AppDataResponse

    private lateinit var viewModel : DownloadViewModel
    lateinit var sharedPreferences : SharedPreferences

    private var listReview = ArrayList<Review>()
    private lateinit var reviewAdapter : ReviewAdapter

    private var listAppDataResponse = ArrayList<AppDataResponse>()
    private val homeCardViewAdapter = HomeCardViewAdapter(listAppDataResponse)

    private lateinit var jwt : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        appModelSelected = intent.getParcelableExtra<AppDataResponse>(EXTRA_APP_MODEL) as AppDataResponse
        viewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()

        initLayout()
        registerReceiver()
    }

    fun setAlarmManager(requestCode : Int, time : Int){
        val mIntent = Intent(this, DownloadReceiver::class.java)
        mIntent.putExtra(DownloadReceiver.EXTRA_REQUEST_CODE, requestCode)
        mIntent.putExtra(EXTRA_APP_MODEL,appModelSelected.package_name)
        mIntent.putExtra(DownloadReceiver.EXTRA_JWT,jwt)
        val mPendingIntent = PendingIntent.getBroadcast(this, requestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        mAlarmManager = this
            .getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager!!.set(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, mPendingIntent
        )
    }

    private fun initSimilarAppsLayout(){
        addAppList()

        rv_list_apps_sejenis.setHasFixedSize(true)
        rv_list_apps_sejenis.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps_sejenis.adapter = homeCardViewAdapter
    }

    private fun initReviewLayout(){
        getReview()
        reviewAdapter = ReviewAdapter(listReview, this)
        rv_review.setHasFixedSize(true)
        rv_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_review.adapter = reviewAdapter
    }

    private fun getReview(){
        if (jwt.isNotEmpty()){
            viewModel.getReview(jwt.toString(), appModelSelected.idApps!!).observe(this, Observer {
                if (it != null){
                    val response = it.reviewDataResponse
                    val data= response?.review
                    if (data?.isNotEmpty()!!){
                        if (data.size <= 2){
                            listReview.add(data[0])
                        }
                        if (data.size == 2){
                            listReview.add(data[1])
                        }
                    }
                    reviewAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun initDownloadButton(){
        btn_download_1.setOnClickListener {
            if (jwt.isEmpty()){
//                showAlertLogin()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
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

    private fun showAlertLogin(){
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Login untuk melanjutkan ?")
        alertDialogBuilder.setPositiveButton("Oke") { _, _ ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
//                    Toast.makeText(this@DownloadActivity,"No Clicked",Toast.LENGTH_SHORT).show()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun initLayout(){
        initSimilarAppsLayout()
        initReviewLayout()
        initDownloadButton()

        val picasso = Picasso.get()
        picasso.load(Utils.baseUrl+"apps/"+appModelSelected.app_icon)
            .into(image_view_download_1)
        app_name_download_1.text = appModelSelected.name
        if (appModelSelected.file_size != null){
            val size = Utils.convertBiteToMB(appModelSelected.file_size!!)
            val fileSize = "$size  MB"
            file_size_download_1.text = fileSize
        }
        review_rating_bar.rating = appModelSelected.rate!!.toFloat()

        if (appModelSelected.rate!!.toInt() > 5){
            textView9.text = "5,0"
        }
        else{
            textView9.text = appModelSelected.rate + ",0"
        }

        review_btn.setOnClickListener {
            if (jwt.isEmpty()){
//                showAlertLogin()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Log.d("jwt.isEmpty()","asup")
            }
            else{
                showInputReviewDialog()
            }
        }

        all_review_btn.setOnClickListener {
            if (jwt.isEmpty()){
//                showAlertLogin()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Log.d("jwt.isEmpty()","asup")
            }
            else{
                val intent = Intent(applicationContext, ReviewActivity::class.java)
                intent.putExtra(ReviewActivity.EXTRA_APP_MODEL,appModelSelected)
                startActivity(intent)
            }
        }

        detail_app_btn.setOnClickListener {
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_APP_MODEL,appModelSelected)
            startActivity(intent)
        }

        btn_search_download.setOnClickListener {
            val intent = Intent(this, SearchAppActivity::class.java)
            intent.putExtra(SearchAppActivity.EXTRA_STRING_SEARCH,"")
            startActivity(intent)
        }

        btn_back_download.setOnClickListener {
            finish()
        }
    }

    private fun showInputReviewDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.input_review_dialog, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.btn_send_review.setOnClickListener {
            val rating = dialogView.findViewById<RatingBar>(R.id.input_rating_review)
            val desc = dialogView.findViewById<EditText>(R.id.input_review_desc)
            viewModel.postReview(jwt, appModelSelected.idApps!!,rating.rating.toInt(),desc.text.toString())
                .observe(this, Observer {
                    if (it != null){
                        Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
                    }
            })
            alertDialog.dismiss()
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

    private fun addAppList(){
        val jwt = sharedPreferences.getString("token", "")

        if (jwt?.length != 0){
            viewModel.getAllApp(jwt.toString()).observe(this, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listAppDataResponse.addAll(data)
                    }
                    homeCardViewAdapter.notifyDataSetChanged()
//                    showLoading(false)
                }
            })
        }
        else{
            viewModel.getAllAppAnonymous(Utils.signature).observe(this, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listAppDataResponse.addAll(data)
                    }
                    homeCardViewAdapter.notifyDataSetChanged()
//                    showLoading(false)
                }
            })
        }
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
                val download: Download = intent.getParcelableExtra("download")!!
                progress_bar_download!!.progress = download.progress
                if (download.progress == 100) {
                    progress_text_download!!.text = "File Download Complete"
                    setAlarmManager(appModelSelected.idApps!!,60* 60 * 1000)
                } else {
                    progress_bar_download.isIndeterminate = false
                    progress_text_download!!.text = String.format("Downloaded (%d/%d) MB", download.currentFileSize, download.totalFileSize)
                }
            }
        }
    }



}