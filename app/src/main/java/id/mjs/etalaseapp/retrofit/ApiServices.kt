package id.mjs.etalaseapp.retrofit

import id.mjs.etalaseapp.model.ForgotPasswordResponse
import id.mjs.etalaseapp.model.LoginRequest
import id.mjs.etalaseapp.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

//    @POST("source/apk/app-pagination-recyclerview.apk")
    @POST("apk/test-apps.apk/")
    @Streaming
    fun getSampleApps(): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/v1/login")
    fun login(@Body data : LoginRequest) : Call<LoginResponse>

    @POST("api/v1/forgot-password")
    fun forgotPassword(@Query("email") email:String) : Call<ForgotPasswordResponse>

}