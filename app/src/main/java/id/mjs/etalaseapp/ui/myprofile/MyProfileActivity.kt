package id.mjs.etalaseapp.ui.myprofile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.item_category.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                picasso.load("https://api-etalase-app.bagustech.id/"+data?.picture.toString())
                    .placeholder(R.drawable.upload_image)
                    .into(my_profile_logo)
                userInfoName.setText(data?.name.toString())
                userInfoEmail.setText(data?.email.toString())
                userInfoBirthday.setText("01 Januari 1990")
            }

        })

    }
}