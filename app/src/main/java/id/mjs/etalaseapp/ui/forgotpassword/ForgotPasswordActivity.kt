package id.mjs.etalaseapp.ui.forgotpassword

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import id.mjs.etalaseapp.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.forgot_password_dialog.view.*


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        btnListener()
    }

    private fun btnListener(){
        bindProgressButton(btn_forgot_password)
        btn_forgot_password.setOnClickListener {
            showLoadingBtn(true)
            val email = et_email_forgot_password.text.toString()
            viewModel.forgotPassword(email).observe(this, Observer {
                if (it != null){
                    if (it.code == "200"){
                        Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
                        showDialog()
                    }
                    else{
                        Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(applicationContext,"Connection Error",Toast.LENGTH_LONG).show()
                }
                showLoadingBtn(false)
            })
        }
    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.forgot_password_dialog, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.btn_back_forgot_password_dialog.setOnClickListener {
            alertDialog.dismiss()
        }
    }

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
}