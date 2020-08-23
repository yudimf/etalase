package id.mjs.etalaseapp.ui.myprofile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_my_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MyProfileActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!

        val jwt = sharedPreferences.getString("token", "")

        ApiMain().services.getUserInfo(jwt).enqueue(object : Callback<UserInfoResponse>{
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.d("fail", t.message.toString())
            }

            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                val data = response.body()?.data
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

        btn_back_my_profile.setOnClickListener {
            finish()
        }

    }
}