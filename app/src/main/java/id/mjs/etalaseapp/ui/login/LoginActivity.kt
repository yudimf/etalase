package id.mjs.etalaseapp.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.ui.createaccount.CreateAccountActivity
import id.mjs.etalaseapp.ui.forgotpassword.ForgotPasswordActivity
import id.mjs.etalaseapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.alert_dialog.view.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var manager: TelephonyManager
    private var stringImei1 : String = "12345678"
    private var stringImei2 : String = "87654321"
    private var firebaseID : String = "test"
    private lateinit var viewModel: LoginViewModel

    companion object {
        const val REQUEST_PHONE_STATE = 8
        const val STATUS_FROM_PROFILE = "status_from_profile"
    }


//    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkPhoneStatePermission()

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        if (sharedPreferences.getString("token","")!!.isNotEmpty()){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Firebase Token ", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result?.token
                if (token != null) {
                    firebaseID = token
                }
                Log.d("Firebase Token ", token.toString())
            })

        try {
            manager =
                getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            stringImei1 = manager.getDeviceId(0)
            stringImei2 = manager.getDeviceId(1)

        }
        catch (ex:Exception){
            Log.e("ex.localizedMessage",ex.localizedMessage)
//            Toast.makeText(this,ex.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        buttonListener()
    }

    override fun onStop() {
        super.onStop()
//        val status = intent.getBooleanExtra(STATUS_FROM_PROFILE, false)
//        if (status){
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun checkPhoneStatePermission(){
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","fail")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_PHONE_STATE)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val status = intent.getBooleanExtra(STATUS_FROM_PROFILE, false)
        if (status){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun btnLoginListener(){

        bindProgressButton(btn_login)
        btn_login.setOnClickListener {
            val email = et_email_login.text.toString()
            val password = et_password_login.text.toString()
            val sdkVersion = Build.VERSION.SDK_INT.toString()
            val data = LoginRequest(
                email,
                password,
                sdkVersion,
                stringImei1,
                stringImei1,
                Build.MANUFACTURER,
                Build.MODEL,
                firebaseID
            )
            btnLoginActive(false)

            viewModel.login(data).observe(this, Observer {
                if (it != null){
                    if (it.code == "201"){
                        Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()

                        sharedPreferences.edit().putString("name",it.data?.name).apply()
                        sharedPreferences.edit().putString("email",it.data?.email).apply()
                        sharedPreferences.edit().putString("token",it.data?.token).apply()

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                    else{
//                        Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
                        val message = it.message
                        showAlertDialog(message!!)
//                        text_login_alert.text = it.message
                    }
                    btnLoginActive(true)
                }
                else{
                    Toast.makeText(applicationContext,"Connection Fail",Toast.LENGTH_SHORT).show()
                    btnLoginActive(true)
                }
            })
        }
    }

    private fun showAlertDialog(description : String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.alert_dialog, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.alert_description.text = description
        dialogView.btn_alert_dialog.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun btnLoginActive(status : Boolean){
        if(status){
            btn_login.setBackgroundColor(resources.getColor(R.color.colorActive))
            btn_login.isClickable = true
            btn_login.hideProgress(R.string.login)
        }
        else{
            btn_login.setBackgroundColor(resources.getColor(R.color.colorDisable))
            btn_login.isClickable = false
            btn_login.showProgress {
                progressColor = Color.WHITE
            }
        }
    }

    private fun buttonListener(){

        btnLoginListener()

        btn_create_account.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tv_forgot_password.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        powered_by.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://ar-tek.co.id/")
            startActivity(openURL)
        }
    }

}