package id.mjs.etalaseapp.retrofit

import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.request.RegisterRequest
import id.mjs.etalaseapp.model.response.LoginResponse
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

    @Headers("Content-Type: application/json")
    @POST("api/v1/register")
    fun register(@Body data : RegisterRequest) : Call<LoginResponse>

}