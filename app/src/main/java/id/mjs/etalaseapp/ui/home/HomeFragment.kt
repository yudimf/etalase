package id.mjs.etalaseapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancj.materialsearchbar.MaterialSearchBar
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CardViewAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.searchapp.SearchAppActivity
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Field


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var list = ArrayList<AppModel>()

    private val cardViewAdapter = CardViewAdapter(list)

    lateinit var sharedPreferences : SharedPreferences

    lateinit var searchBar : MaterialSearchBar

    private var sampleImages = arrayOf(
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg"
    )

    private fun showLoading(status : Boolean){
        val progressBarHome = view?.findViewById<ProgressBar>(R.id.progressBarHome)
        val scrollViewHome = view?.findViewById<ScrollView>(R.id.scroll_view_home)
        if (status){
            Log.d("asup","asup")
            progressBarHome?.visibility = View.VISIBLE
            scrollViewHome?.visibility = View.GONE
        }
        else{
            Log.d("asup22","asup")
            progressBarHome?.visibility = View.GONE
            scrollViewHome?.visibility = View.VISIBLE
        }
    }

    private fun setSearchBar(){

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

        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                Log.d("onSearchConfirmed",text.toString())
                val intent = Intent(context, SearchAppActivity::class.java)
                intent.putExtra(SearchAppActivity.EXTRA_STRING_SEARCH,text.toString())
                startActivity(intent)
            }

        })

    }

    override fun onStop() {
        super.onStop()
        searchBar.text = ""
        searchBar.closeSearch()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val carouselView = root.findViewById(R.id.carouselView) as CarouselView

        searchBar = root.findViewById(R.id.searchBarApp)

        setSearchBar()

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)

        showLoading(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addList()

        rv_list_apps.setHasFixedSize(true)
        rv_list_apps.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps.adapter = cardViewAdapter

        rv_list_apps2.setHasFixedSize(true)
        rv_list_apps2.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps2.adapter = cardViewAdapter

        rv_list_apps3.setHasFixedSize(true)
        rv_list_apps3.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps3.adapter = cardViewAdapter



        cardViewAdapter.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

    }

    private fun addList(){
        val jwt = sharedPreferences.getString("token", "")

        ApiMain().services.getAllApp(jwt).enqueue(object :Callback<ListAppDataResponse>{

            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                Log.d("error","connection")
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
                val data = response.body()?.data
                if (data != null){
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
                        val isEmbededApp = false
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
                            isEmbededApp,
                            fileSize,
                            photoPath!!
                        )
                        list.add(appModel)
                    }
                    cardViewAdapter.notifyDataSetChanged()
                    showLoading(false)
                }
            }

        })

    }

    private var imageListener: ImageListener = ImageListener { position, imageView -> // You can use Glide or Picasso here
        Picasso.get().load(sampleImages[position]).into(imageView)
    }

}

