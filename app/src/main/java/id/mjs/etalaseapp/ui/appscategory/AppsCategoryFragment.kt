package id.mjs.etalaseapp.ui.appscategory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CategoryAdapter
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.ui.listapp.ListAppActivity
import kotlinx.android.synthetic.main.fragment_apps_category.*
import okhttp3.internal.notify

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppsCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppsCategoryFragment : Fragment() {

    private var listCategory = ArrayList<Category>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_apps_category, container, false)

        addDummyList()
        Log.d("size",listCategory.size.toString())
        val adapter = CategoryAdapter(listCategory)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_apps_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Category) {
                val intent = Intent(context,ListAppActivity::class.java)
                intent.putExtra(ListAppActivity.EXTRA_CATEGORY,data)
                startActivity(intent)
            }

        })

        return root
    }


    private fun addDummyList(){
        listCategory.add(Category(1,"Belanja",R.drawable.ic_baseline_shopping_cart_24))
        listCategory.add(Category(2,"Berita",R.drawable.ic_baseline_live_tv_24))
        listCategory.add(Category(3,"Keluarga",R.drawable.ic_baseline_group_24))
        listCategory.add(Category(4,"Buku",R.drawable.ic_baseline_menu_book_24))
        listCategory.add(Category(5,"Olahraga",R.drawable.ic_baseline_directions_bike_24))
        listCategory.add(Category(6,"Music dan Audio",R.drawable.ic_baseline_library_music_24))
        listCategory.add(Category(7,"Game",R.drawable.ic_baseline_videogame_asset_24))
        listCategory.add(Category(8,"Hiburan",R.drawable.ic_baseline_tv_24))
        listCategory.add(Category(9,"Komunikasi",R.drawable.ic_baseline_chat_24))
    }
}