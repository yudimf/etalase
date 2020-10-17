package id.mjs.etalaseapp.ui.myapps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mancj.materialsearchbar.MaterialSearchBar
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.CategoryPagerAdapter
import id.mjs.etalaseapp.adapter.MyAppsPagerAdapter
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.searchapp.SearchAppActivity
import java.lang.reflect.Field

class MyAppsFragment : Fragment() {

    private lateinit var notificationsViewModel: MyAppsViewModel

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private lateinit var searchBar : MaterialSearchBar

    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()

        notificationsViewModel =
            ViewModelProviders.of(this).get(MyAppsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_apps, container, false)

        searchBar = root.findViewById(R.id.searchBarMyApps)
        setSearchBar()

        viewPager = root.findViewById(R.id.my_apps_view_pager)
        tabs = root.findViewById(R.id.my_apps_tab)

        val fragmentAdapter = MyAppsPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        return if (jwt.isEmpty()){
            activity?.finish()
    //                showAlertLogin()
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(LoginActivity.STATUS_FROM_PROFILE,true)
            startActivity(intent)
            null
        } else{
            root
        }
    }

    override fun onStop() {
        super.onStop()
        searchBar.text = ""
        searchBar.closeSearch()
    }

    private fun setSearchBar(){

        try {
            val placeHolder: Field = searchBar.javaClass.getDeclaredField("placeHolder")
            placeHolder.isAccessible = true
            val textView = placeHolder.get(searchBar) as TextView
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            //fix trouble number 2 font-family and number 3 Finally, disable boldness?
            val typeface =
                Typeface.DEFAULT
            //<string name="roboto_medium">fonts/Roboto-Medium.ttf</string>
            textView.typeface = typeface
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onButtonClicked(buttonCode: Int) {

            }

            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                Log.d("onSearchConfirmed",text.toString())
                val intent = Intent(context, SearchAppActivity::class.java)
                intent.putExtra(SearchAppActivity.EXTRA_STRING_SEARCH,text.toString())
                startActivity(intent)
            }

        })

    }
}