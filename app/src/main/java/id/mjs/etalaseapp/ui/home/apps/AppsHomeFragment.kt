package id.mjs.etalaseapp.ui.home.apps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.HomeCardViewAdapter
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.model.response.AdsDataResponse
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.listapp.ListAppActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_home_apps.*


class AppsHomeFragment : Fragment() {

    private lateinit var viewModel: AppsHomeViewModel

    private var listPopularApp = ArrayList<AppDataResponse>()
    private val popularAppsAdapter = HomeCardViewAdapter(listPopularApp)

    private var listBestSellerApp = ArrayList<AppDataResponse>()
    private val bestSellerAppsAdapter = HomeCardViewAdapter(listBestSellerApp)

    private var adsImage = ArrayList<AdsDataResponse>()
    private var sampleImages = ArrayList<String>()

    private lateinit var carouselView :  CarouselView
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home_apps, container, false)
        carouselView = root.findViewById(R.id.carouselView) as CarouselView

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(AppsHomeViewModel::class.java)

        getAds()
        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)
        showLoading(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()

        tv_best_seller_apps.setOnClickListener {
            val intent = Intent(context, ListAppActivity::class.java)
            val category = Category(0,"Aplikasi Terlaris", "")
            intent.putExtra(ListAppActivity.EXTRA_CATEGORY,category)
            intent.putExtra(ListAppActivity.EXTRA_STATUS_FROM_HOME,true)
            intent.putExtra(ListAppActivity.EXTRA_APP_DATA,listBestSellerApp)
            startActivity(intent)
        }

        tv_popular_apps.setOnClickListener {
            val intent = Intent(context, ListAppActivity::class.java)
            val category = Category(0,"Aplikasi Populer", "")
            intent.putExtra(ListAppActivity.EXTRA_CATEGORY,category)
            intent.putExtra(ListAppActivity.EXTRA_STATUS_FROM_HOME,true)
            intent.putExtra(ListAppActivity.EXTRA_APP_DATA,listPopularApp)
            startActivity(intent)
        }

        swipe_layout_home_app.setOnRefreshListener {
            addPopularApps()
            addBestSellerApps()
        }

    }

    private fun showLoading(status : Boolean){
        val progressBarHome = view?.findViewById<ProgressBar>(R.id.progressBarHome)
        val swipeLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_layout_home_app)
        if (status){
            progressBarHome?.visibility = View.VISIBLE
            swipeLayout?.visibility = View.GONE
        }
        else{
            progressBarHome?.visibility = View.GONE
            swipeLayout?.visibility = View.VISIBLE
        }
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
        addPopularApps()
        addBestSellerApps()

        rv_best_seller_apps.setHasFixedSize(true)
        rv_best_seller_apps.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_best_seller_apps.adapter = bestSellerAppsAdapter

        rv_popular_apps.setHasFixedSize(true)
        rv_popular_apps.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_popular_apps.adapter = popularAppsAdapter

        popularAppsAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        bestSellerAppsAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })
    }

    private fun addPopularApps(){
        val jwt = sharedPreferences.getString("token", "")
        listPopularApp.clear()
        if (jwt?.length != 0){
            viewModel.getPopularApps(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listPopularApp.addAll(data)
                    }
                    popularAppsAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_app.isRefreshing = false
            })
        }
        else{
            viewModel.getPopularAppsAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listPopularApp.addAll(data)
                    }
                    popularAppsAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_app.isRefreshing = false
            })
        }
    }

    private fun addBestSellerApps(){
        val jwt = sharedPreferences.getString("token", "")
        listBestSellerApp.clear()
        if (jwt?.length != 0){
            viewModel.getBestSellerApps(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listBestSellerApp.addAll(data)
                    }
                    bestSellerAppsAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_app.isRefreshing = false
            })
        }
        else{
            viewModel.getBestSellerAppsAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listBestSellerApp.addAll(data)
                    }
                    bestSellerAppsAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_app.isRefreshing = false
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

