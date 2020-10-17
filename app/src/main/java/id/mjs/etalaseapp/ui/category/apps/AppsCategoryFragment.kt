package id.mjs.etalaseapp.ui.category.apps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CategoryAdapter
import id.mjs.etalaseapp.model.Category
import id.mjs.etalaseapp.ui.listapp.ListAppActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_apps_category.*

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

    lateinit var sharedPreferences : SharedPreferences
    private var listCategory = ArrayList<Category>()
    lateinit var adapter : CategoryAdapter
    lateinit var viewModel : AppCategoryViewModel

    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_apps_category, container, false)
        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(AppCategoryViewModel::class.java)

        adapter = CategoryAdapter(listCategory)
        recyclerView = root.findViewById(R.id.rv_apps_category)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        onItemClickListener()
        showLoading(true)

        return root
    }

    private fun showLoading(status : Boolean){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBarCategory)
        val recycleView = view?.findViewById<RecyclerView>(R.id.rv_apps_category)
        val swipeRefreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_layout_category_apps)
        Log.d("showLoading",status.toString())
        if (status){
            progressBar?.visibility = View.VISIBLE
            recycleView?.visibility = View.GONE
        }
        else{
            progressBar?.visibility = View.GONE
            recycleView?.visibility = View.VISIBLE
            swipeRefreshLayout?.visibility = View.VISIBLE
        }
    }

    private fun onItemClickListener(){
        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Category) {
                val intent = Intent(context,ListAppActivity::class.java)
                intent.putExtra(ListAppActivity.EXTRA_CATEGORY,data)
                startActivity(intent)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout_category_apps)


        swipeRefreshLayout.setOnRefreshListener {
            Log.d("addCategories","setOnRefreshListener")
            addCategories()
        }

        addCategories()
    }

    private fun addCategories(){
        val jwt = sharedPreferences.getString("token", "")
        listCategory.clear()
        Log.d("addCategories","addCategories")
        if (jwt?.length != 0){
            viewModel.getCategories(jwt.toString()).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.listCategory
                    if (data != null){
                        listCategory.addAll(data)
                    }
                    adapter.notifyDataSetChanged()
                    showLoading(false)
                    swipeRefreshLayout?.isRefreshing = false
                }
            })
        }
        else{
            viewModel.getCategoriesAnonymous(Utils.signature).observe(viewLifecycleOwner, Observer {
                if (it != null){
                    val data = it.listCategory
                    if (data != null){
                        listCategory.addAll(data)
                    }
                    adapter.notifyDataSetChanged()
                    showLoading(false)
                    swipeRefreshLayout?.isRefreshing = false
                }
            })
        }
    }

}