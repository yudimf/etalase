package id.mjs.etalaseapp.ui.listapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ListAppsAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.Download
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.activity_list_app.*

class ListAppActivity : AppCompatActivity() {

    private lateinit var categorySelected : Category

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }

    private var list = ArrayList<AppModel>()
    private var listfiltered = ArrayList<AppModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_app)

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

    }

    private fun filterList(category: Category){
        for (appModel in list){
            if (appModel.categoryId == category.categoryId){
                listfiltered.add(appModel)
            }
        }
    }

    private fun addList(){
        list.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd","",true))
        list.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps","",true))
        list.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift","",true))
        list.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce","",true))
        list.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari","",true))
        list.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe","",true))
        list.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca","",true))
        list.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss","",true))
        list.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone","",true))
        list.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz","",true))
        list.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android","",true))
    }
}