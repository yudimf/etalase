package id.mjs.etalaseapp.ui.forgotpassword

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private fun showLoadingBtn(status : Boolean){
        if(status){
            btn_forgot_password.setBackgroundColor(resources.getColor(R.color.colorDisable))
            btn_forgot_password.isClickable = false
            btn_forgot_password.showProgress {
                progressColor = Color.WHITE
            }
        }
        else{
            btn_forgot_password.setBackgroundColor(resources.getColor(R.color.colorActive))
            btn_forgot_password.isClickable = true
            btn_forgot_password.hideProgress("Lanjutkan")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        bindProgressButton(btn_forgot_password)
        btn_forgot_password.setOnClickListener {
            showLoadingBtn(true)
            ApiMain().services.forgotPassword(et_email_forgot_password.text.toString()).enqueue(object :Callback<ForgotPasswordResponse>{
                override fun onFailure(call: retrofit2.Call<ForgotPasswordResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,"Connection Error",Toast.LENGTH_LONG).show()
                    showLoadingBtn(false)
                }

                override fun onResponse(call: retrofit2.Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                    if (response.body()?.code == "200"){
                        Toast.makeText(applicationContext,"Silahkan Cek Email Anda",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Email tidak terdaftar",Toast.LENGTH_LONG).show()
                    }
                    showLoadingBtn(false)
                }
            })
        }
    }
}