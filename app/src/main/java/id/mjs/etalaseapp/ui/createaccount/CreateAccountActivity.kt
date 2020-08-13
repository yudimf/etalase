package id.mjs.etalaseapp.ui.createaccount

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.request.RegisterRequest
import id.mjs.etalaseapp.model.response.LoginDataResponse
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import kotlinx.android.synthetic.main.activity_create_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreateAccountActivity : AppCompatActivity() {

    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        passwordListener()
        datePickerListener()

        btn_resgitration.setOnClickListener {
            val data = RegisterRequest(
                et_name_register.text.toString(),
                et_password_register.text.toString(),
                et_password_confirmation_register.text.toString(),
                et_email_register.text.toString(),
                et_website_register.text.toString(),
                et_country_register.text.toString(),
                et_address_register.text.toString(),
                2,
                Build.VERSION.SDK_INT.toString()
            )
            ApiMain().services.register(data).enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body()?.code == "500"){
                        Toast.makeText(applicationContext,"Data Tersimpan, Silahkan Login", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Data sudah ada", Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

        btn_to_login.setOnClickListener {
            finish()
        }

    }

    private fun datePickerListener(){
        val c = Calendar.getInstance()
        val years = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        et_date_register.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "MMMM dd, yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                et_date_register.setText(sdf.format(myCalendar.time))
            }, years, month, day)
            dpd.show()
        }
    }

    private fun passwordListener(){
        et_password_register.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password_register.text.toString().length < 6){
                    length_confirmation.text = "Kata sandi harus setidaknya 6 karakter"
                }
                else{
                    length_confirmation.text = ""
                }
            }

        })

        et_password_confirmation_register.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password_register.text.toString() == et_password_confirmation_register.text.toString()){
                    text_confirmation.text = "Kata Sandi Sesuai"
                    text_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDisable))
                }
                else{
                    if (et_password_confirmation_register.text.toString().isNotEmpty()){
                        text_confirmation.text = "Kata Sandi Tidak Sesuai"
                        text_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                    }
                }
            }

        })
    }
}