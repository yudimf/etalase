package id.mjs.etalaseapp.ui.main

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    companion object {
        const val REQUEST_PHONE_STATE = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        Log.d("test", sharedPreferences.getString("token","hah kosong").toString())

        checkPhoneStatePermission()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
    }

    private fun checkPhoneStatePermission(){
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","fail")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_PHONE_STATE
            )
        }
    }
}