package id.mjs.etalaseapp.ui.download

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.HomeCardViewAdapter
import id.mjs.etalaseapp.adapter.MediaAdapter
import id.mjs.etalaseapp.adapter.ReviewAdapter
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.request.UpdateDataRequest
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.Review
import id.mjs.etalaseapp.model.response.ReviewDataResponse
import id.mjs.etalaseapp.receiver.ApkInstalledReceiver
import id.mjs.etalaseapp.receiver.DownloadReceiver
import id.mjs.etalaseapp.services.DownloadService
import id.mjs.etalaseapp.ui.detail.DetailActivity
import id.mjs.etalaseapp.ui.listapp.ListAppActivity
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.review.ReviewActivity
import id.mjs.etalaseapp.ui.searchapp.SearchAppActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.activity_download.review_rating_bar
import kotlinx.android.synthetic.main.input_review_dialog.view.*
import kotlinx.android.synthetic.main.item_review.*
import kotlinx.android.synthetic.main.item_review.view.*
import kotlinx.android.synthetic.main.layout_media_image_fullscreen.view.*
import kotlinx.android.synthetic.main.layout_media_video_fullscreen.view.*
import kotlinx.android.synthetic.main.update_review_dialog.view.*
import java.io.File
import java.io.InputStream
import java.text.DecimalFormat

class DownloadActivity : AppCompatActivity(), Player.EventListener {

    companion object {
        const val MESSAGE_PROGRESS = "message_progress"
        private const val PERMISSION_REQUEST_CODE = 1
        const val EXTRA_APP_MODEL = "extra_app_model"
        const val EXTRA_STATUS_APP = "extra_status_app"
    }

    private var mAlarmManager : AlarmManager? = null

    private lateinit var appModelSelected : AppDataResponse

    private lateinit var viewModel : DownloadViewModel
    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    private var listReview = ArrayList<Review>()
    private lateinit var reviewAdapter : ReviewAdapter
    private var listAppDataResponse = ArrayList<AppDataResponse>()

    private val homeCardViewAdapter = HomeCardViewAdapter(listAppDataResponse)
    private lateinit var emailUser : String
    private var reviewUser = Review()

    private var reviewDataResponse: ReviewDataResponse? = null

    private lateinit var mediaRecyclerView: RecyclerView

    private lateinit var listUrlMedia : ArrayList<String>
    private lateinit var mediaAdapter : MediaAdapter

    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0

    val br = ApkInstalledReceiver()

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, "exoplayer-sample")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        appModelSelected = intent.getParcelableExtra<AppDataResponse>(EXTRA_APP_MODEL) as AppDataResponse
        viewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()
        emailUser = sharedPreferences.getString("email", "").toString()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addDataScheme("package")
//        registerReceiver(br, intentFilter)
    }

    override fun onPause() {
        super.onPause()
//        unregisterReceiver(br)
    }

    override fun onResume() {
        super.onResume()
        mediaLayoutInit()
        showLoading(true)
        getDataFromAPI()
    }

    private fun mediaLayoutInit(){
        mediaRecyclerView = findViewById(R.id.recycler_view_media)
        mediaRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        listUrlMedia = ArrayList()

        if (appModelSelected.media != null){
            for (url in appModelSelected.media!!){
                val data = Utils.baseUrl + url
                listUrlMedia.add(data)
            }
        }

        Log.d("listViewType",listUrlMedia.toString())

        mediaAdapter = MediaAdapter(listUrlMedia)
        mediaRecyclerView.adapter = mediaAdapter

        mediaAdapter.setOnItemClickCallback(object : MediaAdapter.OnItemClickCallback{
            override fun onItemClicked(position: Int) {
                Log.d("mediaAdapterClick",position.toString())

                if (Utils.getItemType(listUrlMedia[position]) == Utils.ITEM_IMAGE){
                    showMediaImageFullscreen(listUrlMedia[position])
                }
                else{
                    showMediaVideoFullscreen(listUrlMedia[position])
                }

            }
        })

    }

    private fun initializePlayer(url : String, dialogView: View) {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer(url, "default")
        dialogView.mediaExoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = false
        simpleExoplayer.addListener(this)
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        Log.d("type",type)
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return if (type == "dash") {
            DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        // handle error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//        if (playbackState == Player.STATE_BUFFERING)
////            mediaProgressBar.visibility = View.VISIBLE
//        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
//            mediaProgressBar.visibility = View.INVISIBLE
    }

    private fun showLoading(status : Boolean){
        if (status){
            progress_bar_download_activity?.visibility = View.VISIBLE
            layout_download_activity?.visibility = View.GONE
        }
        else{
            progress_bar_download_activity?.visibility = View.GONE
            layout_download_activity?.visibility = View.VISIBLE
        }
    }

    private fun getDataFromAPI(){

        var verCode : String = "0"
        if (isAppInstalled(appModelSelected.package_name!!)){
            var pinfo = packageManager.getPackageInfo(appModelSelected.package_name!!, 0)
            verCode = pinfo.versionCode.toString()
        }
        val updateDataRequest = UpdateDataRequest(appModelSelected.package_name,verCode)

        if (jwt.isNotEmpty()){
            viewModel.getDetailApp2(jwt, appModelSelected.idApps!!,updateDataRequest).observe(this, Observer {
                if (it != null){
                    if (it.appDataResponse != null){
                        appModelSelected = it.appDataResponse!!
                        initLayout()
                        setBtnDownload()
                        registerReceiver()
                    }
                }
                showLoading(false)
            })
        }
        else{
            viewModel.getDetailAppAnonymous2(Utils.signature, appModelSelected.idApps!!,updateDataRequest).observe(this, Observer {
                if (it != null){
                    if (it.appDataResponse != null){
                        appModelSelected = it.appDataResponse!!
                        initLayout()
                        setBtnDownload()
                        registerReceiver()
                    }
                }
                showLoading(false)
            })
        }
    }

    private fun setBtnDownload(){
        if (isAppInstalled(appModelSelected.package_name.toString())){
            Log.d("setBtnDownload","installed")
            val status = appModelSelected.apps_status
            if(status == "UPDATE"){
                Log.d("setBtnDownload","update")
                layout_btn_update.visibility = View.VISIBLE
                btn_download_1.visibility = View.INVISIBLE
                btn_download_2.visibility = View.INVISIBLE
                btn_download_1.text = "Update"
            }
            else{
                Log.d("setBtnDownload","no update")
                btn_download_1.visibility = View.INVISIBLE
                btn_download_2.visibility = View.VISIBLE
                layout_btn_update.visibility = View.INVISIBLE
            }
        }
        else{
            Log.d("setBtnDownload","not installed")
            btn_download_1.visibility = View.VISIBLE
            btn_download_2.visibility = View.INVISIBLE
            layout_btn_update.visibility = View.INVISIBLE
        }
    }

    private fun isAppInstalled(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
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

        homeCardViewAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(applicationContext, DownloadActivity::class.java)
                intent.putExtra(EXTRA_APP_MODEL,data)
                startActivity(intent)
            }

        })

        header_aplikasi_serupa.setOnClickListener {
            val intent = Intent(this, ListAppActivity::class.java)
            val category = Category(appModelSelected.category_id!!.toInt(),"Aplikasi Serupa", "")
            intent.putExtra(ListAppActivity.EXTRA_CATEGORY,category)
            startActivity(intent)
        }
    }

    private fun initReviewLayout(){
        reviewAdapter = ReviewAdapter(listReview, appModelSelected.developers?.picture.toString(),
            appModelSelected.developers?.name.toString())
        rv_review.setHasFixedSize(true)
        rv_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_review.adapter = reviewAdapter

        getReview()
    }

    private fun getReview(){
        listReview.clear()

        if (appModelSelected.review != null){
            val data= appModelSelected.review
            Log.d("dataReview",data.toString())
            if (data?.isNotEmpty()!!){
                Log.d("dataReview","asup")
                listReview.add(appModelSelected.review!![0])
                if (data.size > 1){
                    Log.d("dataReview","asup 2")
                    listReview.add(appModelSelected.review!![1])
                }
                Log.d("listReview",listReview.toString())
                reviewAdapter.notifyDataSetChanged()
                rv_review.adapter = reviewAdapter

                var status = false
                for (review in appModelSelected.review!!){
                    if (review.endusers?.email == emailUser){
                        status = true
                        reviewUser = review
                        break
                    }
                }

                if (status){
//                            Toast.makeText(this,"udah komen",Toast.LENGTH_SHORT).show()
                    review_btn.visibility = View.INVISIBLE
                    update_review_btn.visibility = View.VISIBLE
                }
                else{
//                            Toast.makeText(this,"belum komen",Toast.LENGTH_SHORT).show()
                    review_btn.visibility = View.VISIBLE
                    update_review_btn.visibility = View.INVISIBLE
                }
            }
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

        btn_download_update.setOnClickListener {
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
        val textDeveloper = appModelSelected.rate.toString() +"+ "+ appModelSelected.developers?.name.toString()
        apps_developer_download.text = textDeveloper

        var formatAverage = DecimalFormat("#.0").format(appModelSelected.avg_ratings!!.toFloat())
        if (formatAverage == ".0"){
            formatAverage = "0.0"
        }
        rating_viewers.text = formatAverage
        total_reviewer.text = appModelSelected.review?.size.toString()
        review_rating_bar.rating = appModelSelected.avg_ratings!!.toFloat()
        rating_bar_ulasan.rating = appModelSelected.avg_ratings!!.toFloat()
        if (appModelSelected.file_size != null){
            val size = Utils.convertBiteToMB(appModelSelected.file_size!!)
            val fileSize = "$size  MB"
            file_size_download_1.text = fileSize
        }
        if (appModelSelected.review?.size != 0){
            progress_bar_5.progress = appModelSelected.rateDetails?.five!!  * 100 / appModelSelected.review?.size!!
            progress_bar_4.progress = appModelSelected.rateDetails?.four!! * 100 / appModelSelected.review?.size!!
            progress_bar_3.progress = appModelSelected.rateDetails?.three!! * 100 / appModelSelected.review?.size!!
            progress_bar_2.progress = appModelSelected.rateDetails?.two!! * 100 / appModelSelected.review?.size!!
            progress_bar_1.progress = appModelSelected.rateDetails?.one!! * 100 / appModelSelected.review?.size!!
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

        update_review_btn.setOnClickListener {
            showUpdateReviewDialog()
        }

        btn_download_2.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage(appModelSelected.package_name.toString())
            startActivity(intent)
        }

        btn_download_open.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage(appModelSelected.package_name.toString())
            startActivity(intent)
        }

    }

    private fun showUpdateReviewDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.update_review_dialog, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()

        dialogView.update_review_desc.setText(reviewUser.comment.toString())
        dialogView.update_rating_review.rating = reviewUser.ratings!!.toFloat()

        dialogView.btn_update_review.setOnClickListener {
            val rating = dialogView.findViewById<RatingBar>(R.id.update_rating_review)
            val desc = dialogView.findViewById<EditText>(R.id.update_review_desc)
            viewModel.updateReview(jwt, appModelSelected.idApps!!,rating.rating.toInt(),desc.text.toString())
                .observe(this, Observer {
                    if (it != null){
                        Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
                        getReview()
                        reviewAdapter.notifyDataSetChanged()
//                        reloadRate()
                        onResume()
                    }
                })
            alertDialog.dismiss()
        }

        dialogView.btn_delete_review.setOnClickListener {
            viewModel.deleteReview(jwt, appModelSelected.idApps!!)
                .observe(this, Observer {
                    if (it != null){
                        Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
                        getReview()
                        reviewAdapter.notifyDataSetChanged()
//                        reloadRate()
                        onResume()
                        review_btn.visibility = View.VISIBLE
                        update_review_btn.visibility = View.INVISIBLE
                    }
                })
            alertDialog.dismiss()
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
                        getReview()
                        reviewAdapter.notifyDataSetChanged()
                        onResume()
                    }
            })
            alertDialog.dismiss()
        }
    }

    private fun showMediaImageFullscreen(url : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.layout_media_image_fullscreen, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()

        val picasso = Picasso.get()
        picasso.load(url)
            .into(dialogView.media_image_fullscreen)
    }

    private fun showMediaVideoFullscreen(url : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.layout_media_video_fullscreen, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()

        initializePlayer(url, dialogView)
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
        layout_btn_update.visibility = View.INVISIBLE
        progress_bar_download.visibility = View.VISIBLE
        progress_text_download.visibility = View.VISIBLE
        progress_bar_download.isIndeterminate = true
        Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
    }

    private fun addAppList(){
        val jwt = sharedPreferences.getString("token", "")
        listAppDataResponse.clear()
        if (jwt?.length != 0){
            viewModel.getAppsByCategory(jwt.toString(), appModelSelected.category_id!!.toInt()).observe(this, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        for(appData in data){
                            if (appData.idApps != appModelSelected.idApps){
                                listAppDataResponse.add(appData)
                            }
                        }
//                        listAppDataResponse.addAll(data)
                    }
                    homeCardViewAdapter.notifyDataSetChanged()
//                    showLoading(false)
                }
            })
        }
        else{
            viewModel.getAppsByCategoryAnonymous(Utils.signature, appModelSelected.category_id!!.toInt()).observe(this, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        for(appData in data){
                            if (appData.idApps != appModelSelected.idApps){
                                listAppDataResponse.add(appData)
                            }
                        }
//                        listAppDataResponse.addAll(data)
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
                    progress_text_download.visibility = View.INVISIBLE
                    progress_bar_download.isIndeterminate = true
//                    progress_bar_download.visibility = View.INVISIBLE
//                    progress_text_download!!.text = "File Download Complete"
//                    setAlarmManager(appModelSelected.idApps!!,60* 60 * 1000)
                } else {
                    progress_text_download.visibility = View.VISIBLE
                    progress_bar_download.visibility = View.VISIBLE
                    progress_bar_download.isIndeterminate = false
                    val text = String.format("Downloaded %d", download.progress) + "%"
                    progress_text_download!!.text = text
                }

                if (download.isComplete){
                    progress_bar_download.isIndeterminate = false
                    progress_bar_download.visibility = View.GONE
                }

            }
        }
    }



}