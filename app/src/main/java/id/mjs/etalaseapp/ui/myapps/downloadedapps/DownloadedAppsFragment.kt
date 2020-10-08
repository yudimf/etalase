package id.mjs.etalaseapp.ui.myapps.downloadedapps

import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.GridAppsAdapter
import id.mjs.etalaseapp.model.AppInfo
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.model.request.UpdateDataRequest
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.services.UpdateService
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.fragment_downloaded_apps.*
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DownloadedAppsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadedAppsFragment : Fragment() {

    companion object{
        const val MESSAGE_PROGRESS = "message_progress"
        const val MESSAGE_APPS_UPDATED = "message_apps_updated"
    }

    private lateinit var viewModel: DownloadedAppsViewModel

    private var list = ArrayList<AppInfo>()

    private var tempList = ArrayList<AppInfo>()

    lateinit var sharedPreferences : SharedPreferences
    lateinit var jwt : String

    private val updateRequest = UpdateRequest()
    private var listAppUpdate = ArrayList<AppDataResponse>()

    private lateinit var gridAppsAdapter : GridAppsAdapter

    lateinit var dataTemp : AppDataResponse

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(DownloadedAppsViewModel::class.java)
        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()
        updateRequest.data = ArrayList()
        showLoading(true)

        return inflater.inflate(R.layout.fragment_downloaded_apps, container, false)
    }

    private fun showLoading(status : Boolean){
        if (status){
            progress_bar_downloaded_apps?.visibility = View.VISIBLE
            rv_grid_apps?.visibility = View.GONE
        }
        else{
            progress_bar_downloaded_apps?.visibility = View.GONE
            rv_grid_apps?.visibility = View.VISIBLE
        }
    }

    private fun getInstalledApps(){
        Log.d("updateRequest",updateRequest.toString())
        viewModel.getInstalledApps(jwt,updateRequest).observe(viewLifecycleOwner, Observer {
            if (it != null){
                val data = it.data
                if(data != null){
                    listAppUpdate.addAll(data)
                }
                Log.d("listAppDataResponse",listAppUpdate.toString())
                compare()
            }
            showLoading(false)
        })
    }

    private fun compare(){
        var temp = ArrayList<AppInfo>()
        for (dataResponse in listAppUpdate){
            for (appInfo in tempList){
                if (dataResponse.package_name == appInfo.packageName){
                    temp.add(appInfo)
                }
            }
        }
        list.clear()
        list.addAll(temp)
        gridAppsAdapter.notifyDataSetChanged()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (jwt.isNotEmpty()){
            addList()
            getInstalledApps()
        }
        else{
            btn_check_for_update.visibility = View.GONE
            addListInstalledApps()
        }

        rv_grid_apps.layoutManager = GridLayoutManager(context, 3)
        gridAppsAdapter = GridAppsAdapter(list)
        rv_grid_apps.adapter = gridAppsAdapter

        gridAppsAdapter.setOnItemClickCallback(object : GridAppsAdapter.OnItemClickCallback{
            override fun onItemClicked(dataApp: AppInfo) {
                for (appDataResponse in listAppUpdate){
                    if (appDataResponse.package_name == dataApp.packageName){
                        dataTemp = appDataResponse
                    }
                }

                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_STATUS_APP,dataTemp.apps_status)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,dataTemp)
                startActivity(intent)
            }
        })

        btn_check_for_update.setOnClickListener {
            clearCache()
            getApps()
            showProgressUpdate(true)
        }

        registerReceiver()
    }

    private fun clearCache(){
        val path = activity?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/update/"
        Log.d("installUpdatedApps", "Path: $path")
        val directory = File(path)
        val files = directory.listFiles()
//        Log.d("installUpdatedApps", "Size: "+ files.size)
        if (files != null && files.isNotEmpty()){
            for (element in files) {
                element?.delete()
            }
        }
    }

    private fun addList(){
        val pm  = activity?.packageManager
        val packs = activity?.packageManager?.getInstalledPackages(0)
        if (packs != null) {
            for (pack in packs){
                if (!isSystemPackage(pack)){
                    val appName = pm?.let { pack.applicationInfo.loadLabel(it) }.toString()
                    val appIcon = pack.applicationInfo.loadIcon(pm)
                    val appPackage = pack.applicationInfo.packageName
                    val appVersion = pack.versionCode

                    tempList.add(AppInfo(appName,appIcon,appPackage))
                    updateRequest.data?.add(UpdateDataRequest(appPackage,appVersion.toString()))
                    Log.d("appinfo",pm?.let { pack.applicationInfo.loadLabel(it) }.toString())
                    Log.d("appInfoSystem",isSystemPackage(pack).toString())
                }
            }
        }
    }

    private fun addListInstalledApps(){
        val pm  = activity?.packageManager
        val packs = activity?.packageManager?.getInstalledPackages(0)
        if (packs != null) {
            for (pack in packs){
                if (!isSystemPackage(pack)){
                    val appName = pm?.let { pack.applicationInfo.loadLabel(it) }.toString()
                    val appIcon = pack.applicationInfo.loadIcon(pm)
                    val appPackage = pack.applicationInfo.packageName
                    val appVersion = pack.versionCode

                    list.add(AppInfo(appName,appIcon,appPackage))
                    updateRequest.data?.add(UpdateDataRequest(appPackage,appVersion.toString()))
                    Log.d("appinfo",pm?.let { pack.applicationInfo.loadLabel(it) }.toString())
                    Log.d("appInfoSystem",isSystemPackage(pack).toString())
                }
            }
        }
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getApps(){
        viewModel.checkForUpdate(jwt,updateRequest).observe(viewLifecycleOwner, Observer {
            val data = it.data
            if(data != null){
                if (data.size == 0){
//                    showNoAppsFound()
                }
                else{
                    var count = 0
                    for (appData in data){
                        Log.d("apps_status",appData.apps_status.toString())
                        if (appData.apps_status == "UPDATE"){
//                        if (true){
                            listAppUpdate.add(appData)
                            val intent = Intent(context, UpdateService::class.java)
                            intent.putExtra(UpdateService.EXTRA_APP_MODEL,appData)
                            context?.startService(intent)
                            Log.d("listAppUpdate",appData.apk_file.toString())
                            count++
                        }
                    }
                    if (count == 0){
//                        showNoAppsFound()
                    }
                }
            }
            else{
                Log.d("no_apps_check","asup")
//                showNoAppsFound()
            }
//            appsAdapter.notifyDataSetChanged()
            showLoading(false)
        })
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(requireContext())
        val intentFilter = IntentFilter()
        intentFilter.addAction(MESSAGE_PROGRESS)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            Log.d("intentaction", intent?.action.toString())
            if (intent?.action == MESSAGE_PROGRESS) {
                val download: Download = intent.getParcelableExtra("download")!!
                val appName = intent.getStringExtra("appName")
                val isUpdateFinish = intent.getBooleanExtra("isUpdateFinish",false)
                progress_bar_update_all.progress = download.progress
                if (download.progress < 100) {
                    showProgressUpdate(true)
                    progress_bar_update_all.isIndeterminate = false
                    progress_text_update_all.text = String.format("Downloading %s (%d/%d) MB", appName, download.currentFileSize, download.totalFileSize)
                }

                if (isUpdateFinish){
                    showProgressUpdate(false)
                }

            }
            else if (intent?.action == MESSAGE_APPS_UPDATED){
                Log.d("asupasup","asup")
                showProgressUpdate(false)
            }

        }

    }

    private fun showProgressUpdate(status : Boolean){
        if (!status){
            btn_check_for_update.visibility = View.VISIBLE
            progress_bar_update_all.visibility = View.INVISIBLE
            progress_text_update_all.visibility = View.INVISIBLE
        }
        else{
            btn_check_for_update.visibility = View.INVISIBLE
            progress_bar_update_all.visibility = View.VISIBLE
            progress_text_update_all.visibility = View.VISIBLE
            progress_bar_update_all.isIndeterminate = true
        }
    }

}