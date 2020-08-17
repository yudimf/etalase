package id.mjs.etalaseapp.retrofit

import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.request.RegisterRequest
import id.mjs.etalaseapp.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

//    @POST("source/apk/app-pagination-recyclerview.apk")
    @POST("apk/test-apps.apk/")
    @Streaming
    fun getSampleApps(): Call<ResponseBody>

    @POST("apk/{path}/")
    @Streaming
    fun getApps(@Path("path") path : String): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/v1/login")
    fun login(@Body data : LoginRequest) : Call<LoginResponse>

    @POST("api/v1/forgot-password")
    fun forgotPassword(@Query("email") email:String) : Call<ForgotPasswordResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/register")
    fun register(@Body data : RegisterRequest) : Call<LoginResponse>

    @Multipart
    @POST("api/v1/register")
    fun registerUser(@Part("email") email : RequestBody,
                     @Part("password") password : RequestBody,
                     @Part("name") name : RequestBody,
                     @Part("sdk_version") sdk_version : RequestBody,
                     @Part("birthday") birthday : RequestBody,
                     @Part photo : MultipartBody.Part) : Call<LoginResponse>

    @GET("api/v1/apps/list-category")
    fun getCategories(@Header("jwt") jwt : String?) : Call<CategoryResponse>

    @GET("api/v1/apps")
    fun getApps(@Header("jwt") jwt : String?, @Query("category_id") category_id : Int) : Call<ListAppDataResponse>

    @GET("api/v1/apps")
    fun getAllApp(@Header("jwt") jwt : String?) : Call<ListAppDataResponse>

    @GET("api/v1/user-info")
    fun getUserInfo(@Header("jwt") jwt : String?) : Call<UserInfoResponse>

}