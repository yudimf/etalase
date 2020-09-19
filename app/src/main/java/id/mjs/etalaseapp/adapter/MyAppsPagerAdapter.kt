package id.mjs.etalaseapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.mjs.etalaseapp.ui.myapps.downloadedapps.DownloadedAppsFragment

class MyAppsPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return DownloadedAppsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Aplikasi Saya"
    }

    override fun getCount(): Int = 1
}