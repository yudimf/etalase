package id.mjs.etalaseapp.ui.myapps.downloadedapps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.GridAppsAdapter
import id.mjs.etalaseapp.model.AppInfo
import id.mjs.etalaseapp.model.request.UpdateDataRequest
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.checkforupdate.CheckForUpdateActivity
import id.mjs.etalaseapp.ui.checkforupdate.CheckForUpdateViewModel
import id.mjs.etalaseapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_downloaded_apps.*


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

    private lateinit var viewModel: DownloadedAppsViewModel

    private var list = ArrayList<AppInfo>()

    private var tempList = ArrayList<AppInfo>()

    lateinit var sharedPreferences : SharedPreferences
    lateinit var jwt : String

    private val updateRequest = UpdateRequest()
    private var listAppDataResponse = ArrayList<AppDataResponse>()

    private lateinit var gridAppsAdapter : GridAppsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(DownloadedAppsViewModel::class.java)
        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()
        updateRequest.data = ArrayList()

        return inflater.inflate(R.layout.fragment_downloaded_apps, container, false)
    }

    private fun getInstalledApps(){
        Log.d("updateRequest",updateRequest.toString())
        viewModel.getInstalledApps(jwt,updateRequest).observe(viewLifecycleOwner, Observer {
            if (it != null){
                val data = it.data
                if(data != null){
                    listAppDataResponse.addAll(data)
                }
                Log.d("listAppDataResponse",listAppDataResponse.toString())
                compare()
            }
        })
    }

    private fun compare(){
        var temp = ArrayList<AppInfo>()
        for (dataResponse in listAppDataResponse){
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

        btn_check_for_update.setOnClickListener {
            if (jwt.isEmpty()){
                val intent = Intent(context , LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(context, CheckForUpdateActivity::class.java)
                startActivity(intent)
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

}