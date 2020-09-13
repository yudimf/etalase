package id.mjs.etalaseapp.ui.splashscreen

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    private val splashTimeOut : Long = 2000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
//            startActivity(Intent(this, LoginActivity::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashTimeOut)

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