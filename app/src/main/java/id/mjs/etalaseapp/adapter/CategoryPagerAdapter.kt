package id.mjs.etalaseapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.mjs.etalaseapp.ui.appscategory.AppsCategoryFragment
import id.mjs.etalaseapp.ui.gamescategory.GamesCategoryFragment

class CategoryPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AppsCategoryFragment()
            }
            else -> {
                return GamesCategoryFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Aplikasi"
            else -> {
                return "Games"
            }
        }
    }

    override fun getCount(): Int = 2
}