package id.mjs.etalaseapp.ui.home.games

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import kotlinx.android.synthetic.main.fragment_games_home.*

class GamesHomeFragment : Fragment() {

    private lateinit var viewModel: GamesHomeViewModel

    private var listBestSellerGame = ArrayList<AppDataResponse>()
    private val bestSellerGamesAdapter = HomeCardViewAdapter(listBestSellerGame)

    private var listPopularGame = ArrayList<AppDataResponse>()
    private val popularGamesAdapter = HomeCardViewAdapter(listPopularGame)

    private var adsImage = ArrayList<AdsDataResponse>()
    private var sampleImages = ArrayList<String>()

    private lateinit var carouselView : CarouselView
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_games_home, container, false)

        carouselView = root.findViewById(R.id.carouselViewGame) as CarouselView

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(GamesHomeViewModel::class.java)

        getAds()
        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)
        showLoading(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()

        tv_best_seller_games.setOnClickListener {
            val intent = Intent(context, ListAppActivity::class.java)
            val category = Category(0,"Game Terlaris", "")
            intent.putExtra(ListAppActivity.EXTRA_CATEGORY,category)
            intent.putExtra(ListAppActivity.EXTRA_STATUS_FROM_HOME,true)
            intent.putExtra(ListAppActivity.EXTRA_APP_DATA,listBestSellerGame)
            startActivity(intent)
        }

        tv_popular_games.setOnClickListener {
            val intent = Intent(context, ListAppActivity::class.java)
            val category = Category(0,"Game Populer", "")
            intent.putExtra(ListAppActivity.EXTRA_CATEGORY,category)
            intent.putExtra(ListAppActivity.EXTRA_STATUS_FROM_HOME,true)
            intent.putExtra(ListAppActivity.EXTRA_APP_DATA,listPopularGame)
            startActivity(intent)
        }

        swipe_layout_home_game.setOnRefreshListener {
            addBestSellerGames()
            addPopularGames()
        }
    }

    private fun setRecycleView(){
        addBestSellerGames()
        addPopularGames()

        rv_best_seller_games.setHasFixedSize(true)
        rv_best_seller_games.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_best_seller_games.adapter = bestSellerGamesAdapter

        rv_popular_games.setHasFixedSize(true)
        rv_popular_games.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_popular_games.adapter = popularGamesAdapter

        bestSellerGamesAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        popularGamesAdapter.setOnItemClickCallback(object : HomeCardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppDataResponse) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })
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

    private fun showLoading(status : Boolean){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBarHomeG)
        val swipeLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_layout_home_game)
        if (status){
            progressBar?.visibility = View.VISIBLE
            swipeLayout?.visibility = View.GONE
        }
        else{
            progressBar?.visibility = View.GONE
            swipeLayout?.visibility = View.VISIBLE
        }
    }

    private fun addBestSellerGames(){
        val jwt = sharedPreferences.getString("token", "")
        listBestSellerGame.clear()
        if (jwt?.length != 0){
            viewModel.getBestSellerGames(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listBestSellerGame.addAll(data)
                    }
                    bestSellerGamesAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_game.isRefreshing = false
            })
        }
        else{
            viewModel.getBestSellerAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listBestSellerGame.addAll(data)
                    }
                    bestSellerGamesAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_game.isRefreshing = false
            })
        }
    }

    private fun addPopularGames(){
        val jwt = sharedPreferences.getString("token", "")
        listPopularGame.clear()
        if (jwt?.length != 0){
            viewModel.getPopularGames(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listPopularGame.addAll(data)
                    }
                    popularGamesAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_game.isRefreshing = false
            })
        }
        else{
            viewModel.getPopularAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.data
                    if (data != null) {
                        listPopularGame.addAll(data)
                    }
                    popularGamesAdapter.notifyDataSetChanged()
                }
                showLoading(false)
                swipe_layout_home_game.isRefreshing = false
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