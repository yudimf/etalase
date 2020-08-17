package id.mjs.etalaseapp.ui.listapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ListAppsAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.Download
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
        categorySelected = intent.getParcelableExtra(EXTRA_CATEGORY) as Category

        addList()

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
        ApiMain().services.getApps(jwt,categorySelected.categoryId).enqueue(object : Callback<ListAppDataResponse>{
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
                        val description = "app.description"
                        val isEmbededApp = false
                        val fileSize = 11
                        val photoPath = app.app_icon

                        val appModel = AppModel(
                            categoryId!!.toInt(),
                            photo,
                            name!!,
                            downloadLink!!,
                            playStoreLink,
                            description!!,
                            isEmbededApp,
                            fileSize!!,
                            photoPath!!
                        )

                        listfiltered.add(appModel)
                    }
                    listAppsAdapter.notifyDataSetChanged()
                    recycle_view_list_app.adapter = listAppsAdapter
                }
            }

        })

    }

    private fun filterList(category: Category){
        for (appModel in list){
            if (appModel.categoryId == category.categoryId){
                listfiltered.add(appModel)
            }
        }
    }

    private fun addList(){
//        list.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd",getString(R.string.desc_tokopedia),true,44))
//        list.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps",getString(R.string.desc_alfacart),true,17))
//        list.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift",getString(R.string.desc_alfagift),true,18))
//        list.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce",getString(R.string.desc_blibli),true,26))
//        list.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari",getString(R.string.desc_matahari_dept),true,21))
//        list.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe",getString(R.string.desc_babe),true,34))
//        list.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca",getString(R.string.desc_baca),true,20))
//        list.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss",getString(R.string.desc_detik),true,18))
//        list.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone",getString(R.string.desc_cnn),true,14))
//        list.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz",getString(R.string.desc_catfiz),true,14))
//        list.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android",getString(R.string.desc_vidio),true,13))
//        list.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63))
    }
}