package id.mjs.etalaseapp.ui.listapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ListAppsAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.activity_list_app.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAppActivity : AppCompatActivity() {

    private lateinit var categorySelected : Category

    lateinit var sharedPreferences : SharedPreferences


    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }

    private var list = ArrayList<AppModel>()
    private var listfiltered = ArrayList<AppModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_app)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        categorySelected = intent.getParcelableExtra<Category>(EXTRA_CATEGORY) as Category

        filterList(categorySelected)

        val listAppsAdapter = ListAppsAdapter(listfiltered)
        recycle_view_list_app.setHasFixedSize(true)
        recycle_view_list_app.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle_view_list_app.adapter = listAppsAdapter

        category_name.text = categorySelected.categoryName

        listAppsAdapter.setOnItemClickCallback(object : ListAppsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(applicationContext,DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        val jwt = sharedPreferences.getString("token", "")
        ApiMain().services.getAppsByCategory(jwt,categorySelected.categoryId).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
                val data = response.body()?.data
                if (data != null) {
                    for (app in data){
                        val categoryId = app.category_id
                        val photo = 0
                        val name = app.name
                        val downloadLink = app.apk_file
                        val playStoreLink = "kosong"
                        var description : String? = ""
                        if (app.description != null){
                            description = app.description
                        }
                        val isEmbeddedApp = false
                        var fileSize = 0
                        if (app.file_size != null){
                            fileSize = app.file_size!! / 1024 / 1024
                        }
                        val photoPath = app.app_icon

                        val appModel = AppModel(
                            categoryId!!.toInt(),
                            photo,
                            name!!,
                            downloadLink!!,
                            playStoreLink,
                            description!!,
                            isEmbeddedApp,
                            fileSize,
                            photoPath!!
                        )

                        listfiltered.add(appModel)
                    }
                    listAppsAdapter.notifyDataSetChanged()
                    recycle_view_list_app.adapter = listAppsAdapter
                }
            }

        })

        btn_back_list_app.setOnClickListener {
            finish()
        }

    }

    private fun filterList(category: Category){
        for (appModel in list){
            if (appModel.categoryId == category.categoryId){
                listfiltered.add(appModel)
            }
        }
    }

}