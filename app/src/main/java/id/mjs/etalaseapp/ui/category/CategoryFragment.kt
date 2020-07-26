package id.mjs.etalaseapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CategoryPagerAdapter
import id.mjs.etalaseapp.model.Category

class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?): View? {
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_category, container, false)

        viewPager = root.findViewById(R.id.category_view_pager)
        tabs = root.findViewById(R.id.category_tab)

        val fragmentAdapter = CategoryPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)


        return root
    }


}