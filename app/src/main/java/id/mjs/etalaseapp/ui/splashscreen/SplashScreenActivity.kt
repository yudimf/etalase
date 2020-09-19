package id.mjs.etalaseapp.ui.splashscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.listapp.ListAppViewModel
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_PHONE_STATE = 8
    }

    private val splashTimeOut : Long = 3000 // 1 sec

    lateinit var sharedPreferences : SharedPreferences
    lateinit var jwt : String

    lateinit var viewModel: SplashScreenViewModel

    private fun checkPhoneStatePermission() : Boolean{
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","fail")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_PHONE_STATE
            )
            return false
        }
        else{
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        jwt = sharedPreferences.getString("token", "").toString()

        if (checkPhoneStatePermission()){
            initProcess()
        }

    }

    private fun initProcess(){
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_PHONE_STATE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initProcess()
            }
            else{
                finish()
            }
        }

    }
}