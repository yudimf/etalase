package id.mjs.etalaseapp.ui.checkforupdate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.AppsAdapter
import id.mjs.etalaseapp.model.AppInfo
import id.mjs.etalaseapp.model.request.UpdateDataRequest
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.activity_check_for_update.*
import kotlinx.android.synthetic.main.activity_list_app.*

class CheckForUpdateActivity : AppCompatActivity() {

    private lateinit var viewModel: CheckForUpdateViewModel
    lateinit var sharedPreferences : SharedPreferences
    lateinit var jwt : String
    private val updateRequest = UpdateRequest()

    private var listAppDataResponse = ArrayList<AppDataResponse>()
    private lateinit var appsAdapter : AppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_for_update)

        updateRequest.data = ArrayList()
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()
        viewModel = ViewModelProvider(this).get(CheckForUpdateViewModel::class.java)

        initLayout()
        addList()
        getApps()
        showLoading(true)

        Log.d("jwt",jwt.toString())
        Log.d("updateRequest",updateRequest.toString())
    }

    private fun initLayout(){
        appsAdapter = AppsAdapter(listAppDataResponse)
        recycle_view_update.setHasFixedSize(true)
        recycle_view_update.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle_view_update.adapter = appsAdapter

        appsAdapter.setOnItemClickCallback(object : AppsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(applicationContext, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        btn_back_update.setOnClickListener {
            finish()
        }
    }

    private fun getApps(){
        viewModel.checkForUpdate(jwt,updateRequest).observe(this, Observer {
            val data = it.data
            if(data != null){
                listAppDataResponse.addAll(data)
            }
            appsAdapter.notifyDataSetChanged()
            showLoading(false)
        })
    }

    private fun showLoading(status : Boolean){
        if (status){
            layout_progress_update?.visibility = View.VISIBLE
            recycle_view_update?.visibility = View.GONE
        }
        else{
            layout_progress_update?.visibility = View.GONE
            recycle_view_update?.visibility = View.VISIBLE
        }
    }

    private fun addList(){
        val pm  = packageManager
        val packs = packageManager?.getInstalledPackages(0)
        if (packs != null) {
            for (pack in packs){

                if (!isSystemPackage(pack)){
                    val appName = pm?.let { pack.applicationInfo.loadLabel(it) }.toString()
                    val appIcon = pack.applicationInfo.loadIcon(pm)
                    val appPackage = pack.applicationInfo.packageName
                    val appVersion = pack.versionCode

                    updateRequest.data?.add(UpdateDataRequest(appPackage,appVersion.toString()))
                    Log.d("appinfo",pm?.let { pack.applicationInfo.loadLabel(it) }.toString())
                    Log.d("appInfoSystem",isSystemPackage(pack).toString())
                }
            }
        }
        Log.d("appinfo",updateRequest.toString())
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}