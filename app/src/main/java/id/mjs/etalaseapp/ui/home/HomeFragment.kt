package id.mjs.etalaseapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CardViewAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var list = ArrayList<AppModel>()
    private var list2 = ArrayList<AppModel>()
    private var list3 = ArrayList<AppModel>()

    private var sampleImages = arrayOf(
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg",
        "https://raw.githubusercontent.com/yudimf/sample_image/master/black_ic.jpeg"
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
        addList()
        val cardViewAdapter = CardViewAdapter(list)
        rv_list_apps.setHasFixedSize(true)
        rv_list_apps.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps.adapter = cardViewAdapter

        addList2()
        val cardViewAdapter2 = CardViewAdapter(list2)
        rv_list_apps2.setHasFixedSize(true)
        rv_list_apps2.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps2.adapter = cardViewAdapter2

        addList3()
        val cardViewAdapter3 = CardViewAdapter(list3)
        rv_list_apps3.setHasFixedSize(true)
        rv_list_apps3.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rv_list_apps3.adapter = cardViewAdapter3

        cardViewAdapter.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        cardViewAdapter2.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

        cardViewAdapter3.setOnItemClickCallback(object : CardViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: AppModel) {
                val intent = Intent(context, DownloadActivity::class.java)
                intent.putExtra(DownloadActivity.EXTRA_APP_MODEL,data)
                startActivity(intent)
            }
        })

    }

    private fun addList(){
        list.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63))
    }

    private fun addList2(){
        list2.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss",getString(R.string.desc_detik),true,18))
        list2.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone",getString(R.string.desc_cnn),true,14))
        list2.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz",getString(R.string.desc_catfiz),true,14))
        list2.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android",getString(R.string.desc_vidio),true,13))
        list2.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63))
        list2.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd",getString(R.string.desc_tokopedia),true,44))
        list2.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca",getString(R.string.desc_baca),true,20))
        list2.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps",getString(R.string.desc_alfacart),true,17))
        list2.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift",getString(R.string.desc_alfagift),true,18))
        list2.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce",getString(R.string.desc_blibli),true,26))
        list2.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari",getString(R.string.desc_matahari_dept),true,21))
        list2.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe",getString(R.string.desc_babe),true,34))
    }

    private fun addList3(){
        list3.add(AppModel(9,R.drawable.ic_catfiz,"Catfiz","","https://play.google.com/store/apps/details?id=com.catfiz",getString(R.string.desc_catfiz),true,14))
        list3.add(AppModel(1,R.drawable.ic_alfagift,"Alfagift","","https://play.google.com/store/apps/details?id=com.alfamart.alfagift",getString(R.string.desc_alfagift),true,18))
        list3.add(AppModel(1,R.drawable.ic_blibli,"Blibli","","https://play.google.com/store/apps/details?id=blibli.mobile.commerce",getString(R.string.desc_blibli),true,26))
        list3.add(AppModel(8,R.drawable.ic_vidio,"Vidio.com","","https://play.google.com/store/apps/details?id=com.vidio.android",getString(R.string.desc_vidio),true,13))
        list3.add(AppModel(7,R.drawable.icon_wawa,"Wawa Adventure Games","","https://play.google.com/store/apps/details?id=games.wawa",getString(R.string.desc_wawa),false,63))
        list3.add(AppModel(1,R.drawable.ic_mataharimall,"Matahari Mall","","https://play.google.com/store/apps/details?id=app.ndtv.matahari",getString(R.string.desc_matahari_dept),true,21))
        list3.add(AppModel(2,R.drawable.ic_babe,"Babe - Baca Berita","","https://play.google.com/store/apps/details?id=id.co.babe",getString(R.string.desc_babe),true,34))
        list3.add(AppModel(1,R.drawable.ic_alfacart,"Alfacart","","https://play.google.com/store/apps/details?id=com.alfacart.apps",getString(R.string.desc_alfacart),true,17))
        list3.add(AppModel(2,R.drawable.ic_baca,"Baca Berita, Video, Komunitas Game & Nama Keren","","https://play.google.com/store/apps/details?id=com.jakarta.baca",getString(R.string.desc_baca),true,20))
        list3.add(AppModel(2,R.drawable.ic_detik,"Detik","","https://play.google.com/store/apps/details?id=org.detikcom.rss",getString(R.string.desc_detik),true,18))
        list3.add(AppModel(2,R.drawable.ic_cnn,"CNN","","https://play.google.com/store/apps/details?id=com.cnn.mobile.android.phone",getString(R.string.desc_cnn),true,14))
        list3.add(AppModel(1,R.drawable.ic_tokped,"Tokopedia","","https://play.google.com/store/apps/details?id=com.tokopedia.tkpd",getString(R.string.desc_tokopedia),true,44))
    }

    private var imageListener: ImageListener = ImageListener { position, imageView -> // You can use Glide or Picasso here
        Picasso.get().load(sampleImages[position]).into(imageView)
    }

}