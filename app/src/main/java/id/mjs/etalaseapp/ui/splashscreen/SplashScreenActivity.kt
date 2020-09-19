package id.mjs.etalaseapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.listapp.ListAppViewModel
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    private val splashTimeOut : Long = 3000 // 1 sec

    lateinit var sharedPreferences : SharedPreferences

    lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        val jwt = sharedPreferences.getString("token", "").toString()

        if (jwt.isNotEmpty()){
            viewModel.getUserInfo(jwt).observe(this, Observer {
                if (it == null){
                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            })
        }
        else{
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, splashTimeOut)
        }


    }



    private fun test(){
        val apps =
            packageManager.getInstalledApplications(0)

        for (app in apps) {
            if (app.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) > 0) {
                Log.d("System Apps",app.packageName)
            } else {
                Log.d("User Apps",app.packageName)
                val pinfo = packageManager.getPackageInfo(app.packageName, 0)
                val versionNumber = pinfo.versionCode
                val versionName = pinfo.versionName
                Log.d("User Version Number",versionNumber.toString())
                Log.d("User Version Name",versionName)
            }
        }

        var androidVersion = Build.VERSION.SDK_INT
        Log.d("androidVersion",androidVersion.toString())
    }
}