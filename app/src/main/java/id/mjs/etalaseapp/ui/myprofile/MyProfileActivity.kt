package id.mjs.etalaseapp.ui.myprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.ui.searchapp.SearchAppViewModel
import id.mjs.etalaseapp.utils.DatePickerHelper
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MyProfileActivity : AppCompatActivity(), SupportedDatePickerDialog.OnDateSetListener {

    lateinit var sharedPreferences : SharedPreferences

    private lateinit var viewModel: MyProfileViewModel

    private lateinit var photoFile : File
    private var isFileAssign : Boolean = false
    private lateinit var filePath : String

    private val myCalendar = Calendar.getInstance()
    private var stringBirthDate : String = "1996-01-01"
    lateinit var datePicker: DatePickerHelper

    private lateinit var jwt : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        jwt = sharedPreferences.getString("token", "").toString()
        getUserData(jwt)

        btnListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                isFileAssign = true
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                my_profile_logo.setImageURI(fileUri)
                //You can get File object from intent
                photoFile = ImagePicker.getFile(data)!!
                Log.d("photoFile",photoFile.toString())
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
        btn_back_my_profile.setOnClickListener {
            finish()
        }

        tv_change_profile_picture.setOnClickListener {
            ImagePicker.with(this)
//                .cropSquare()
                .compress(1024)
//                .maxResultSize(620, 620)
                .galleryOnly()
                .start()
        }

        btn_update_profile.setOnClickListener {
            val requestFile : RequestBody?
            var bodyPhoto : MultipartBody.Part? = null
            if (isFileAssign){
                requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),photoFile)
                bodyPhoto = MultipartBody.Part.createFormData("photo",photoFile.name,requestFile)
                Log.d("isFileAssign",photoFile.toString())
            }

            val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userInfoEmail.text.toString())
            val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),userInfoName.text.toString())
            val birthday = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),stringBirthDate)

            viewModel.updateProfile(jwt,email,name,birthday,bodyPhoto).observe(this, androidx.lifecycle.Observer {
                if (it==null){
                    Toast.makeText(applicationContext,"Connection Fail", Toast.LENGTH_LONG).show()
                }
                else{
                    when (it.code) {
                        "500" -> {
                            Toast.makeText(applicationContext,it.message, Toast.LENGTH_LONG).show()
                        }
                        "505" -> {
                            Toast.makeText(applicationContext,it.message, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(applicationContext,it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })

        }

        userInfoBirthday.setOnClickListener {
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
        userInfoBirthday.setText(sdf2.format(myCalendar.time))
    }

    private fun getUserData(jwt : String){
        viewModel.getUserInfo(jwt).observe(this, androidx.lifecycle.Observer {
            if (it != null){
                Log.d("getUserInfo","getUserInfo")
                val data = it?.data
                val picasso = Picasso.get()
                picasso.load(Utils.baseUrl+data?.picture.toString())
//                    .placeholder(R.drawable.upload_image)
                    .into(my_profile_logo)
                userInfoName.setText(data?.name.toString())
                userInfoEmail.setText(data?.email.toString())

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val date = sdf.parse(data?.birthDate)
                val cal = Calendar.getInstance()
                cal.time = date

                val myFormat2 = "dd-MM-yyyy" //In which you need put here
                val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
                sdf2.format(cal.time)
                userInfoBirthday.setText(sdf2.format(cal.time))
            }
        })
    }
}