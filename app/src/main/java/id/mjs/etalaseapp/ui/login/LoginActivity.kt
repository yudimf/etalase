package id.mjs.etalaseapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.main.MainActivity
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.createaccount.CreateAccountActivity
import id.mjs.etalaseapp.ui.forgotpassword.ForgotPasswordActivity
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.response.LoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        if (sharedPreferences.getString("token","")!!.isNotEmpty()){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        bindProgressButton(btn_login)
        btn_login.setOnClickListener {
            val email = et_email_login.text.toString()
            val password = et_password_login.text.toString()
            val sdkVersion = Build.VERSION.SDK_INT.toString()
            val data = LoginRequest(
                email,
                password,
                sdkVersion
            )
            btn_login.setBackgroundColor(resources.getColor(R.color.colorDisable))
            btn_login.isClickable = false
            btn_login.showProgress {
                progressColor = Color.WHITE
            }
            ApiMain().services.login(data).enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Connection Fail",Toast.LENGTH_SHORT).show()
                    btn_login.setBackgroundColor(resources.getColor(R.color.colorActive))
                    btn_login.isClickable = true
                    btn_login.hideProgress(R.string.done)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body()?.code == "201"){
                        Toast.makeText(applicationContext,"Login Berhasil",Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit().putString("name",response.body()?.data?.name).apply()
                        sharedPreferences.edit().putString("email",response.body()?.data?.email).apply()
                        sharedPreferences.edit().putString("token",response.body()?.data?.token).apply()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext,"Username / Password Salah",Toast.LENGTH_SHORT).show()
                    }
                    btn_login.setBackgroundColor(resources.getColor(R.color.colorActive))
                    btn_login.isClickable = true
                    btn_login.hideProgress(R.string.login)
                }

            })

        }

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