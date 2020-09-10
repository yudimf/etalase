package id.mjs.etalaseapp.ui.searchapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancj.materialsearchbar.MaterialSearchBar
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ListAppsAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.login.LoginViewModel
import id.mjs.etalaseapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_list_app.*
import kotlinx.android.synthetic.main.activity_search_app.*
import java.lang.reflect.Field


class SearchAppActivity : AppCompatActivity() {

    lateinit var stringSearch : String

    companion object {
        const val EXTRA_STRING_SEARCH = "extra_string_search"
    }

    private lateinit var jwt : String

    private lateinit var viewModel: SearchAppViewModel

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var searchBar : MaterialSearchBar

    private lateinit var listAppsAdapter : ListAppsAdapter

    private var list = ArrayList<AppModel>()
    private var listfiltered = ArrayList<AppModel>()

    private fun setSearchBar(){

        searchBar = findViewById(R.id.searchBar)

        try {
            val placeHolder: Field = searchBar.javaClass.getDeclaredField("placeHolder")
            placeHolder.isAccessible = true
            val textView = placeHolder.get(searchBar) as TextView
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            //fix trouble number 2 font-family and number 3 Finally, disable boldness?
            val typeface =
                Typeface.DEFAULT
            //<string name="roboto_medium">fonts/Roboto-Medium.ttf</string>
            textView.typeface = typeface
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        searchBar.performClick()
        searchBar.text = stringSearch

        searchBar.setOnSearchActionListener(object :MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {
                Log.d("onButtonClicked",buttonCode.toString())
                when (buttonCode) {
                    MaterialSearchBar.BUTTON_BACK -> {
                        Log.d("asup","backbutton")
                    }
                }
            }

            override fun onSearchStateChanged(enabled: Boolean) {
                Log.d("onSearchStateChanged",enabled.toString())

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                Log.d("onSearchConfirmed",text.toString())
                val appName = text.toString()
                searchData(appName)
            }

        })
    }

    private fun searchData(appName : String){
        showLoading(true)
        viewModel.searchAppByName(jwt.toString(),appName).observe( this@SearchAppActivity, Observer {
            populateData(it)
        })
    }

    private fun populateData(data : ListAppDataResponse){
        listfiltered.clear()
        if (data.data != null) {
            if (data.data!!.size == 0){
                showNotFoundStatus()
                Log.d("ASup","kadieu")
            }
            else{
                for (app in data.data!!){
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
                showLoading(false)
                recycle_view_list_search_app.adapter = listAppsAdapter
            }
        }
        else{
            showNotFoundStatus()
        }
    }

    private fun showNotFoundStatus(){
        progress_bar_search_app.visibility = View.GONE
        recycle_view_list_search_app.visibility = View.GONE
        app_not_found.visibility = View.VISIBLE
    }

    private fun showLoading(status : Boolean){
        app_not_found.visibility = View.GONE
        if (status){
            progress_bar_search_app.visibility = View.VISIBLE
            recycle_view_list_search_app.visibility = View.GONE
        }
        else{
            progress_bar_search_app.visibility = View.GONE
            recycle_view_list_search_app.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_app)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        stringSearch = intent.getStringExtra(EXTRA_STRING_SEARCH) as String

        viewModel = ViewModelProvider(this).get(SearchAppViewModel::class.java)

        Log.d("stringSearch",stringSearch)

        setSearchBar()

        listAppsAdapter = ListAppsAdapter(listfiltered)
        recycle_view_list_search_app.setHasFixedSize(true)
        recycle_view_list_search_app.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycle_view_list_search_app.adapter = listAppsAdapter

        listAppsAdapter.setOnItemClickCallback(object : ListAppsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(applicationContext, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        jwt = sharedPreferences.getString("token", "").toString()

        searchData(stringSearch)
    }
}