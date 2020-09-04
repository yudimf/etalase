package id.mjs.etalaseapp.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class UserRepository {

    fun login(data : LoginRequest) : MutableLiveData<LoginResponse> {
        val loginResponse = MutableLiveData<LoginResponse>()

        ApiMain().services.login(data).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResponse.postValue(null)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful){
                    loginResponse.postValue(response.body())
                }
                else{
                    val gson = Gson()
                    val adapter: TypeAdapter<LoginResponse> =
                        gson.getAdapter(LoginResponse::class.java)
                    try {
                        if (response.errorBody() != null)
                            loginResponse.postValue(adapter.fromJson(response.errorBody()!!.string()))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        return loginResponse
    }

    fun register(email : RequestBody,
                 password : RequestBody,
                 name : RequestBody,
                 sdkVersion : RequestBody,
                 birthday : RequestBody,
                 bodyPhoto : MultipartBody.Part?,
                 imei1 : RequestBody,
                 imei2 : RequestBody,
                 deviceBrand : RequestBody,
                 deviceModel : RequestBody) : MutableLiveData<LoginResponse>{

        val registerResponse = MutableLiveData<LoginResponse>()

        ApiMain().services.registerUser(email,password,name,sdkVersion,birthday,bodyPhoto,imei1,imei2,deviceBrand,deviceModel).enqueue(object:Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("error",t.message)
                registerResponse.postValue(null)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("registerresponse",response.body()?.message)
                if(response.isSuccessful){
                    registerResponse.postValue(response.body())
                }
            }
        })
        return registerResponse
    }

}