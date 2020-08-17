package id.mjs.etalaseapp.ui.gamescategory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CategoryAdapter
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.ui.listapp.ListAppActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GamesCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamesCategoryFragment : Fragment() {

    private var listCategory = ArrayList<Category>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_games_category, container, false)

        addDummyList()
        val adapter = CategoryAdapter(listCategory)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_games_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Category) {
                val intent = Intent(context, ListAppActivity::class.java)
                intent.putExtra(ListAppActivity.EXTRA_CATEGORY,data)
                startActivity(intent)
            }

        })
        return root
    }

    private fun addDummyList(){
//        listCategory.add(Category(71,"Game Edukasi",R.drawable.ic_book))
//        listCategory.add(Category(72,"Game Teka Teki",R.drawable.ic_puzzle))
//        listCategory.add(Category(73,"Game Keluarga",R.drawable.ic_family))
//        listCategory.add(Category(74,"Game Kasual",R.drawable.ic_tshirt))
//        listCategory.add(Category(75,"Game Aksi Petualangan",R.drawable.ic_binocular))
//        listCategory.add(Category(76,"Game Simulasi",R.drawable.ic_simulation))
//        listCategory.add(Category(77,"Game Musik",R.drawable.ic_music))
//        listCategory.add(Category(78,"Game Strategi",R.drawable.ic_cheess))
    }
}