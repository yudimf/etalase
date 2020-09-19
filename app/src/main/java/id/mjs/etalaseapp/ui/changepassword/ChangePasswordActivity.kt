package id.mjs.etalaseapp.ui.changepassword

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.myprofile.MyProfileViewModel
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    private var validatePassword : Boolean = false
    private var validatePasswordConfirmation : Boolean = false
    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var jwt : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        jwt = sharedPreferences.getString("token", "").toString()

        passwordListener()
        setView()
    }

    private fun setView(){

        btn_back_change_password.setOnClickListener {
            finish()
        }

        btn_change_password_confirmation.setOnClickListener {
            val email =  et_email_change.text.toString()
            val newPassword = et_password_change.text.toString()
            val oldPassword = et_old_password_change.text.toString()
            viewModel.changePassword(jwt,email,oldPassword,newPassword).observe(this, Observer {
                if (it != null){
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                    text_change_password_alert.text = it.message
                }
            })
        }

        viewModel.getUserInfo(jwt).observe(this, Observer {
            et_email_change.setText(it.data?.email.toString())
        })



    }

    private fun passwordListener(){
        et_password_change.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password_change.text.toString().length < 6){
                    length_confirmation_pwchange.text = "Kata sandi harus setidaknya 6 karakter"
//                    length_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                    validatePassword = false
                }
                else{
                    length_confirmation_pwchange.text = ""
                    validatePassword = true
                }

                if (et_password_change.text.toString() == et_password_change_confirmation.text.toString()){
                    text_confirmation_pwchange.text = "Kata Sandi Sesuai"
                    text_confirmation_pwchange.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDisable))
                    validatePasswordConfirmation = true
                }
                else{
                    if (et_password_change_confirmation.text.toString().isNotEmpty()){
                        text_confirmation_pwchange.text = "Kata Sandi Tidak Sesuai"
                        text_confirmation_pwchange.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                        validatePasswordConfirmation = false
                    }
                }
            }

        })

        et_password_change_confirmation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password_change.text.toString() == et_password_change_confirmation.text.toString()){
                    text_confirmation_pwchange.text = "Kata Sandi Sesuai"
                    text_confirmation_pwchange.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDisable))
                    validatePasswordConfirmation = true
                }
                else{
                    if (et_password_change_confirmation.text.toString().isNotEmpty()){
                        text_confirmation_pwchange.text = "Kata Sandi Tidak Sesuai"
                        text_confirmation_pwchange.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                        validatePasswordConfirmation = false
                    }
                }
            }

        })
    }
}