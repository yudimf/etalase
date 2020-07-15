package id.mjs.etalaseapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CardViewAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.ui.createaccount.CreateAccountActivity
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var list = ArrayList<AppModel>()

    private var sampleImages = arrayOf(
        "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg",
        "https://raw.githubusercontent.com/sayyam/carouselview/master/sample/src/main/res/drawable/image_1.jpg",
        "https://raw.githubusercontent.com/sayyam/carouselview/master/sample/src/main/res/drawable/image_2.jpg"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val carouselView = root.findViewById(R.id.carouselView) as CarouselView


        carouselView.pageCount = sampleImages.size
        carouselView.setImageListener(imageListener)





        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val rvListApps = root.findViewById(R.id.rv_list_apps) as RecyclerView
//        val rvListApps2 = root.findViewById(R.id.rv_list_apps2) as RecyclerView
//        val rvListApps3 = root.findViewById(R.id.rv_list_apps3) as RecyclerView
        addList()
        val cardViewAdapter = CardViewAdapter(list)
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
                startActivity(intent)
            }
        })
    }

    private fun addList(){
        for (i in 0 until 9){
            list.add(AppModel("link", "Aplikasi $i","desc"))
        }
    }

    private var imageListener: ImageListener = ImageListener { position, imageView -> // You can use Glide or Picasso here
        Picasso.get().load(sampleImages[position]).into(imageView)
    }

}