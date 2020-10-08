package id.mjs.etalaseapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.mjs.etalaseapp.ui.home.apps.AppsHomeFragment
import id.mjs.etalaseapp.ui.home.games.GamesHomeFragment

class HomePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AppsHomeFragment()
            }
            else -> {
                return GamesHomeFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Aplikasi"
            else -> {
                return "Permainan"
            }
        }
    }

    override fun getCount(): Int = 2
}