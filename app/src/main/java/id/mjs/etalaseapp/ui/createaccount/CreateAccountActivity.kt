package id.mjs.etalaseapp.ui.createaccount

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.request.RegisterRequest
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.utils.DatePickerHelper
import kotlinx.android.synthetic.main.activity_create_account.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreateAccountActivity : AppCompatActivity(), SupportedDatePickerDialog.OnDateSetListener {

    private val myCalendar = Calendar.getInstance()
    private lateinit var photoFile : File
    private lateinit var filePath : String
    private var isFileAssign : Boolean = false
    private var validatePassword : Boolean = false
    private var validatePasswordConfirmation : Boolean = false
    private var stringBirthDate : String = "1996-01-01"

    lateinit var datePicker: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        datePicker = DatePickerHelper(this, true)

        Log.d("isvalidemail",isValidEmail("yudi").toString())

        passwordListener()
//        datePickerListener()
//        btnListener()

        create_account_image.setOnClickListener {
            ImagePicker.with(this)
//                .cropSquare()
                .compress(1024)
//                .maxResultSize(620, 620)
                .galleryOnly()
                .start()
        }

        btn_to_login.setOnClickListener {
            finish()
        }

        btn_resgitration.setOnClickListener {

            Log.d("validateinput",validateInput().toString())

            val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_email_register.text.toString())
            val password = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_password_register.text.toString())
            val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_name_register.text.toString())
            val sdkVersion = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Build.VERSION.SDK_INT.toString())
            val birthday = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),stringBirthDate)

            if (validateInput()){
                val requestFile : RequestBody?
                var bodyPhoto : MultipartBody.Part? = null
                if (isFileAssign){
                    requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),photoFile)
                    bodyPhoto = MultipartBody.Part.createFormData("photo",photoFile.name,requestFile)
                }

                ApiMain().services.registerUser(email,password,name,sdkVersion,birthday,bodyPhoto).enqueue(object:Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext,"Error",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        Log.d("registerresponse",response.body()?.message)
                        if (response.body()?.code == "500"){
                            Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else if(response.body()?.code == "505"){
                            Toast.makeText(applicationContext,response.body()?.message,Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
            else{
                Toast.makeText(applicationContext,"Silahkan Lengkapi Data",Toast.LENGTH_LONG).show()
            }

        }

        et_date_register.setOnClickListener {
            showSpinnerDate()
        }

    }

    private fun showSpinnerDate(){
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        SupportedDatePickerDialog(this, R.style.SpinnerDatePickerDialogTheme, this, year, month, dayOfMonth).show()
    }

    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                stringBirthDate = sdf.format(myCalendar.time)
                Log.d("birthDate",stringBirthDate)

                val myFormat2 = "dd-MM-yyyy" //In which you need put here
                val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
                et_date_register.setText(sdf2.format(myCalendar.time))
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    private fun validateInput() : Boolean{

        return (validatePassword &&
                validatePasswordConfirmation &&
                isValidEmail(et_email_register.text.toString()) &&
                !et_email_register.text.isNullOrEmpty() &&
                !et_password_register.text.isNullOrEmpty() &&
                !et_name_register.text.isNullOrEmpty() &&
                !et_date_register.text.isNullOrEmpty()
                )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {

                isFileAssign = true

                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                create_account_image.setImageURI(fileUri)

                //You can get File object from intent
                photoFile = ImagePicker.getFile(data)!!

                //You can also get File Path from intent
                filePath = ImagePicker.getFilePath(data)!!
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnListener(){
        btn_resgitration.setOnClickListener {
            val data = RegisterRequest(
                et_name_register.text.toString(),
                et_password_register.text.toString(),
                et_password_confirmation_register.text.toString(),
                et_email_register.text.toString(),
                "",
                "et_country_register.text.toString()",
                "et_address_register.text.toString()",
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
                val myFormat = "yyyy-MM-dd" //In which you need put here
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
//                    length_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                    validatePassword = false
                }
                else{
                    length_confirmation.text = ""
                    validatePassword = true
                }

                if (et_password_register.text.toString() == et_password_confirmation_register.text.toString()){
                    text_confirmation.text = "Kata Sandi Sesuai"
                    text_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDisable))
                    validatePasswordConfirmation = true
                }
                else{
                    if (et_password_confirmation_register.text.toString().isNotEmpty()){
                        text_confirmation.text = "Kata Sandi Tidak Sesuai"
                        text_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                        validatePasswordConfirmation = false
                    }
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
                    validatePasswordConfirmation = true
                }
                else{
                    if (et_password_confirmation_register.text.toString().isNotEmpty()){
                        text_confirmation.text = "Kata Sandi Tidak Sesuai"
                        text_confirmation.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDanger))
                        validatePasswordConfirmation = false
                    }
                }
            }

        })
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "yyyy-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        stringBirthDate = sdf.format(myCalendar.time)
        Log.d("birthDate",stringBirthDate)

        val myFormat2 = "dd-MM-yyyy" //In which you need put here
        val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
        et_date_register.setText(sdf2.format(myCalendar.time))
    }
}