package id.mjs.etalaseapp.retrofit

import id.mjs.etalaseapp.model.LoginBody
import id.mjs.etalaseapp.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

//    @POST("source/apk/app-pagination-recyclerview.apk")
    @POST("repo/com.uberspot.a2048_25.apk")
    @Streaming
    fun getSampleApps(): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/v1/login")
    fun login(@Body data : LoginBody) : Call<LoginResponse>

}