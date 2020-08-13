package id.mjs.etalaseapp.ui.myapps

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
import id.mjs.etalaseapp.adapter.MyAppsPagerAdapter

class MyAppsFragment : Fragment() {

    private lateinit var notificationsViewModel: MyAppsViewModel

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(MyAppsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_apps, container, false)

        viewPager = root.findViewById(R.id.my_apps_view_pager)
        tabs = root.findViewById(R.id.my_apps_tab)

        val fragmentAdapter = MyAppsPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        return root
    }
}