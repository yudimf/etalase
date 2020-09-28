package id.mjs.etalaseapp.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.HomeCardViewAdapter
import id.mjs.etalaseapp.model.response.AdsDataResponse
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.searchapp.SearchAppActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.reflect.Field


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var listAppDataResponse = ArrayList<AppDataResponse>()
    private val homeCardViewAdapter = HomeCardViewAdapter(listAppDataResponse)

    private var listAppDataResponse2 = ArrayList<AppDataResponse>()
    private val homeCardViewAdapter2 = HomeCardViewAdapter(listAppDataResponse2)

    private var listAppDataResponse3 = ArrayList<AppDataResponse>()
    private val homeCardViewAdapter3 = HomeCardViewAdapter(listAppDataResponse3)

    private lateinit var carouselView :  CarouselView

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var searchBar : MaterialSearchBar

//    private var sampleImages = arrayOf(
//        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
//        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
//        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg"
//    )

    private var sampleImages = ArrayList<String>()

    private var adsImage = ArrayList<AdsDataResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        carouselView = root.findViewById(R.id.carouselView) as CarouselView

        searchBar = root.findViewById(R.id.searchBarApp)

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        getAds()
        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)
        showLoading(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchBar()
        setRecycleView()
    }

    private fun showLoading(status : Boolean){
        val progressBarHome = view?.findViewById<ProgressBar>(R.id.progressBarHome)
        val scrollViewHome = view?.findViewById<ScrollView>(R.id.scroll_view_home)
        if (status){
            progressBarHome?.visibility = View.VISIBLE
            scrollViewHome?.visibility = View.GONE
        }
        else{
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

        searchBar.setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener{
            override fun OnItemDeleteListener(position: Int, v: View?) {
                Log.d("OnItemDeleteListener","OnItemDeleteListener")
                val newList = searchBar.lastSuggestions
                newList.removeAt(position)
                searchBar.updateLastSuggestions(newList)
                searchBar.clearSuggestions()
            }

            override fun OnItemClickListener(position: Int, v: View?) {
                Log.d("OnItemClickListener",position.toString())
            }

        })

    }

    override fun onStop() {
        super.onStop()
        searchBar.text = ""
        searchBar.closeSearch()
    }

    private fun getAds(){

        carouselView.setImageClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("http://" + adsImage[it].link)
            startActivity(openURL)
        }

        viewModel.getAds(Utils.signature).observe(viewLifecycleOwner, Observer {
            if (it != null){
                if (it.code == 100){
                    for (data in it.data!!){
                        sampleImages.add(Utils.baseUrl + data.picture.toString())
                        adsImage.add(data)
                        Log.d("adsvm",Utils.baseUrl + data.picture.toString())
                    }
                    carouselView.pageCount = sampleImages.size
                    carouselView.setImageListener(imageListener)
                }
            }
        })

    }

    private fun setRecycleView(){
        addAppList()

        rv_list_apps.setHasFixedSize(true)
        rv_list_apps.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps.adapter = homeCardViewAdapter

        rv_list_apps2.setHasFixedSize(true)
        rv_list_apps2.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps2.adapter = homeCardViewAdapter2

        rv_list_apps3.setHasFixedSize(true)
        rv_list_apps3.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps3.adapter = homeCardViewAdapter3

        homeCardViewAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        homeCardViewAdapter2.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        homeCardViewAdapter3.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })
    }

    private fun addAppList(){
        val jwt = sharedPreferences.getString("token", "")
        listAppDataResponse.clear()
        if (jwt?.length != 0){
            viewModel.getAllApp(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        for (appData in data){
//                            listAppDataResponse.addAll(data)
                            if (appData.type == "Games"){
                                listAppDataResponse.add(appData)
                            }
                            else{
                                listAppDataResponse2.add(appData)
                                listAppDataResponse3.add(appData)
                            }
                        }
                    }
                    homeCardViewAdapter.notifyDataSetChanged()
                    homeCardViewAdapter2.notifyDataSetChanged()
                    homeCardViewAdapter3.notifyDataSetChanged()
                }
                showLoading(false)
            })
        }
        else{
            viewModel.getAllAppAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        for (appData in data){
//                            listAppDataResponse.addAll(data)
                            if (appData.type == "Games"){
                                listAppDataResponse.add(appData)
                            }
                            else{
                                listAppDataResponse2.add(appData)
                                listAppDataResponse3.add(appData)
                            }
                        }
                    }
                    homeCardViewAdapter.notifyDataSetChanged()
                    homeCardViewAdapter2.notifyDataSetChanged()
                    homeCardViewAdapter3.notifyDataSetChanged()
                }
                showLoading(false)
            })
        }
    }

    private var imageListener: ImageListener = ImageListener { position, imageView -> // You can use Glide or Picasso here
        Picasso.get()
            .load(sampleImages[position])
            .fit()
            .centerInside()
            .into(imageView)

        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
    }

}

