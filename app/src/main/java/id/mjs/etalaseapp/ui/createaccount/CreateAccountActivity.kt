package id.mjs.etalaseapp.ui.createaccount

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.utils.DatePickerHelper
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_create_account.create_account_image
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.verification_dialog.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreateAccountActivity : AppCompatActivity(), SupportedDatePickerDialog.OnDateSetListener {

    private val myCalendar = Calendar.getInstance()
    private lateinit var photoFile : File
    private lateinit var filePath : String
    lateinit var manager: TelephonyManager
    lateinit var stringImei1 : String
    lateinit var stringImei2 : String
    private var isFileAssign : Boolean = false
    private var validatePassword : Boolean = false
    private var validatePasswordConfirmation : Boolean = false
    private var stringBirthDate : String = "1996-01-01"
    lateinit var datePicker: DatePickerHelper
    private var firebaseID : String = ""

    private lateinit var viewModel : CreateAccountViewModel

    private fun checkPhoneStatePermission(){
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","fail")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        viewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)
        datePicker = DatePickerHelper(this, true)

        checkPhoneStatePermission()
        manager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        stringImei1 = manager.getDeviceId(0)
        stringImei2 = manager.getDeviceId(1)

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

        passwordListener()
        btnListener()
        btnRegistrationListener()

    }

    private fun btnRegistrationListener(){
        bindProgressButton(btn_resgitration)
        btn_resgitration.setOnClickListener {
            val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_email_register.text.toString())
            val password = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_password_register.text.toString())
            val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),et_name_register.text.toString())
            val sdkVersion = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Build.VERSION.SDK_INT.toString())
            val birthday = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),stringBirthDate)
            val imei1 = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),stringImei1)
            val imei2 = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),stringImei2)
            val brand = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Build.MANUFACTURER)
            val model = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),Build.MODEL)
            val firebaseId = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),firebaseID)

            if (validateInput()){
                showLoadingBtnRegistration(true)
                val requestFile : RequestBody?
                var bodyPhoto : MultipartBody.Part? = null
                if (isFileAssign){
                    requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),photoFile)
                    bodyPhoto = MultipartBody.Part.createFormData("photo",photoFile.name,requestFile)
                }

                viewModel.register(email,password,name,sdkVersion,birthday,bodyPhoto,imei1,imei2,brand,model,firebaseId).observe(this, androidx.lifecycle.Observer {
                    if (it==null){
                        Toast.makeText(applicationContext,"Connection Fail",Toast.LENGTH_LONG).show()
                    }
                    else{
                        if (it.code == "500"){
                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
                            showDialog()
                        }
                        else if(it.code == "505"){
                            Toast.makeText(applicationContext,it.message,Toast.LENGTH_LONG).show()
                        }
                    }
                    showLoadingBtnRegistration(false)
                })
            }
            else{
//                Toast.makeText(applicationContext,"Silahkan Lengkapi Data",Toast.LENGTH_LONG).show()
                showAlertDialog("Silahkan Lengkapi Data")
            }

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

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup = findViewById<ViewGroup>(R.id.content)
        val dialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.verification_dialog, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.btn_back_verification_dialog.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
    }

    private fun btnListener(){
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

        et_date_register.setOnClickListener {
            showSpinnerDate()
        }
    }

    private fun showLoadingBtnRegistration(status : Boolean){
        if(status){
            btn_resgitration.setBackgroundColor(resources.getColor(R.color.colorDisable))
            btn_resgitration.isClickable = false
            btn_resgitration.showProgress {
                progressColor = Color.WHITE
            }
        }
        else{
            btn_resgitration.setBackgroundColor(resources.getColor(R.color.colorActive))
            btn_resgitration.isClickable = true
            btn_resgitration.hideProgress("BUAT AKUN")
        }
    }

    private fun showSpinnerDate(){
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
        SupportedDatePickerDialog(this, R.style.SpinnerDatePickerDialogTheme, this, year, month, dayOfMonth).show()
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
                !et_date_register.text.isNullOrEmpty())
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