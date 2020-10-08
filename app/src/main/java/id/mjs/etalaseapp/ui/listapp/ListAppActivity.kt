package id.mjs.etalaseapp.ui.listapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.AppsAdapter
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_list_app.*

class ListAppActivity : AppCompatActivity() {

    private lateinit var categorySelected : Category
    lateinit var sharedPreferences : SharedPreferences
    lateinit var viewModel: ListAppViewModel
    private var listAppDataResponse = ArrayList<AppDataResponse>()
    private lateinit var appsAdapter : AppsAdapter

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_STATUS_FROM_HOME = "extra_status_from_home"
        const val EXTRA_APP_DATA = "extra_app_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_app)
        showLoading(true)
        viewModel = ViewModelProvider(this).get(ListAppViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        categorySelected = intent.getParcelableExtra<Category>(EXTRA_CATEGORY) as Category
        val status = intent.getBooleanExtra(EXTRA_STATUS_FROM_HOME, false)

        initLayout()

        if (status){
            val data = intent.getParcelableArrayListExtra<AppDataResponse>(EXTRA_APP_DATA)
            if (data != null && data.isNotEmpty()){
                listAppDataResponse.addAll(data!!)
            }
            showLoading(false)
        }
        else{
            getAppsByCategory()
        }


    }

    private fun initLayout(){
        appsAdapter = AppsAdapter(listAppDataResponse)
        recycle_view_list_app.setHasFixedSize(true)
        recycle_view_list_app.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle_view_list_app.adapter = appsAdapter

        category_name_list_app.text = categorySelected.categoryName

        appsAdapter.setOnItemClickCallback(object : AppsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(applicationContext,DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        btn_back_list_app.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(status : Boolean){
        if (status){
            layout_progress_list_app?.visibility = View.VISIBLE
            recycle_view_list_app?.visibility = View.GONE
        }
        else{
            layout_progress_list_app?.visibility = View.GONE
            recycle_view_list_app?.visibility = View.VISIBLE
        }
    }

    private fun getAppsFromHome(){
        val jwt = sharedPreferences.getString("token", "")
        if (jwt?.length != 0){
            viewModel.getAppsByCategory(jwt.toString(),categorySelected.categoryId).observe(this,
                Observer {
                    if(it != null){
                        val data = it.data
                        if (data != null){
                            Log.d("getAppsByCategory","jwt")
                            listAppDataResponse.addAll(data)
                        }
                        appsAdapter.notifyDataSetChanged()
                    }
                    showLoading(false)
                })
        }
        else{
            viewModel.getAppsByCategoryAnonymous(Utils.signature,categorySelected.categoryId).observe(this,
                Observer {
                    if(it != null){
                        val data = it.data
                        if (data != null){
                            Log.d("getAppsByCategory","signature")
                            listAppDataResponse.addAll(data)
                        }
                        appsAdapter.notifyDataSetChanged()
                    }
                    showLoading(false)
                })
        }
    }

    private fun getAppsByCategory(){
        val jwt = sharedPreferences.getString("token", "")
        if (jwt?.length != 0){
            viewModel.getAppsByCategory(jwt.toString(),categorySelected.categoryId).observe(this,
                Observer {
                    if(it != null){
                        val data = it.data
                        if (data != null){
                            Log.d("getAppsByCategory","jwt")
                            listAppDataResponse.addAll(data)
                        }
                        appsAdapter.notifyDataSetChanged()
                    }
                    showLoading(false)
                })
        }
        else{
            viewModel.getAppsByCategoryAnonymous(Utils.signature,categorySelected.categoryId).observe(this,
                Observer {
                    if(it != null){
                        val data = it.data
                        if (data != null){
                            Log.d("getAppsByCategory","signature")
                            listAppDataResponse.addAll(data)
                        }
                        appsAdapter.notifyDataSetChanged()
                    }
                    showLoading(false)
                })
        }
    }

}