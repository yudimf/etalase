package id.mjs.etalaseapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.model.response.UserInfoResponse
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

    fun updateProfile(jwt : String,
                      email : RequestBody,
                      name : RequestBody,
                      birthday : RequestBody,
                      bodyPhoto : MultipartBody.Part?) : MutableLiveData<LoginResponse>{
        val updateProfileResponse = MutableLiveData<LoginResponse>()

        ApiMain().services.updateProfile(jwt, email, name, birthday, bodyPhoto).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                updateProfileResponse.postValue(null)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    updateProfileResponse.postValue(response.body())
                }
            }

        })

        return updateProfileResponse
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
                Log.d("error",t.message.toString())
                registerResponse.postValue(null)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("registerresponse",response.body()?.message.toString())
                if(response.isSuccessful){
                    registerResponse.postValue(response.body())
                }
            }
        })
        return registerResponse
    }

    fun forgotPassword(email : String) : MutableLiveData<ForgotPasswordResponse>{
        val forgotPasswordResponse = MutableLiveData<ForgotPasswordResponse>()

        ApiMain().services.forgotPassword(email).enqueue(object : Callback<ForgotPasswordResponse>{
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                forgotPasswordResponse.postValue(null)
            }

            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful){
                    forgotPasswordResponse.postValue(response.body())
                }
            }
        })
        return forgotPasswordResponse
    }

    fun getUserInfo(jwt : String) : MutableLiveData<UserInfoResponse>{
        val userInfoResponse = MutableLiveData<UserInfoResponse>()

        ApiMain().services.getUserInfo(jwt).enqueue(object : Callback<UserInfoResponse>{
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                userInfoResponse.postValue(null)
            }

            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                if (response.isSuccessful){
                    userInfoResponse.postValue(response.body())
                }
            }

        })

        return userInfoResponse
    }

    fun changePassword(jwt : String?, email : String, oldPassword : String, newPassword : String) : MutableLiveData<ForgotPasswordResponse>{
        val changePasswordResponse = MutableLiveData<ForgotPasswordResponse>()

        ApiMain().services.changePassword(jwt,email,oldPassword,newPassword).enqueue(object : Callback<ForgotPasswordResponse>{
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                changePasswordResponse.postValue(null)
            }

            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful){
                    changePasswordResponse.postValue(response.body())
                }
            }

        })

        return changePasswordResponse
    }

}