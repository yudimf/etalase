package id.mjs.etalaseapp.ui.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        btn_forgot_password.setOnClickListener {
            ApiMain().services.forgotPassword(et_email_forgot_password.text.toString()).enqueue(object :Callback<ForgotPasswordResponse>{
                override fun onFailure(call: retrofit2.Call<ForgotPasswordResponse>, t: Throwable) {

                }

                override fun onResponse(call: retrofit2.Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                    if (response.body()?.code == "200"){
                        Toast.makeText(applicationContext,"Silahkan Cek Email Anda",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Email tidak terdaftar",Toast.LENGTH_LONG).show()
                    }
                }


            })

        }

    }
}