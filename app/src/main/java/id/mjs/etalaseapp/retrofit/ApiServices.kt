package id.mjs.etalaseapp.retrofit

import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.request.RegisterRequest
import id.mjs.etalaseapp.model.request.UpdateRequest
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
    fun getAppsByCategory(@Path("path") path : String): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("api/v1/login")
    fun login(@Body data : LoginRequest) : Call<LoginResponse>

    @POST("api/v1/forgot-password")
    fun forgotPassword(@Query("email") email:String) : Call<ForgotPasswordResponse>

    @POST("api/v1/user-info/change-password")
    fun changePassword(@Header("jwt") jwt : String?,
                       @Query("email") email : String,
                       @Query("old_password") oldPassword : String,
                       @Query("new_password") newPassword : String) : Call<ForgotPasswordResponse>

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
                     @Part photo : MultipartBody.Part?,
                     @Part("imei_1") imei1 : RequestBody,
                     @Part("imei_2") imei2 : RequestBody,
                     @Part("device_brand") deviceBrand : RequestBody,
                     @Part("device_model") deviceModel : RequestBody) : Call<LoginResponse>

    @Multipart
    @POST("api/v1/user-info/update-profile")
    fun updateProfile(@Header("jwt") jwt : String?,
                      @Part("email") email : RequestBody,
                      @Part("name") name : RequestBody,
                      @Part("birthday") birthday : RequestBody,
                      @Part photo : MultipartBody.Part?) : Call<LoginResponse>

    @GET("api/v1/apps/list-category?type_apps=Application")
    fun getCategoriesApp(@Header("jwt") jwt : String?) : Call<CategoryResponse>

    @GET("api/v1/apps/list-category?type_apps=Application")
    fun getCategoriesAnonymousApp(@Header("signature") signature : String?) : Call<CategoryResponse>

    @GET("api/v1/apps/list-category?type_apps=Games")
    fun getCategoriesGames(@Header("jwt") jwt : String?) : Call<CategoryResponse>

    @GET("api/v1/apps/list-category?type_apps=Games")
    fun getCategoriesAnonymousGames(@Header("signature") signature : String?) : Call<CategoryResponse>

    @GET("api/v1/apps")
    fun getAppsByCategory(@Header("jwt") jwt : String?, @Query("category_id") category_id : Int) : Call<AppResponse>

    @GET("api/v1/apps")
    fun getAppsByCategoryAnonymous(@Header("signature") signature : String?, @Query("category_id") category_id : Int) : Call<AppResponse>

    @GET("api/v1/apps")
    fun getAppsByName(@Header("jwt") jwt : String?, @Query("search") appName : String) : Call<AppResponse>

    @GET("api/v1/apps")
    fun getAppsByNameAnonymous(@Header("signature") signature: String?, @Query("search") appName : String) : Call<AppResponse>

    @GET("api/v1/apps")
    fun getAllApp(@Header("jwt") jwt : String?) : Call<AppResponse>

    @GET("api/v1/apps")
    fun getAllAppAnonymous(@Header("signature") signature: String?) : Call<AppResponse>

    @GET("api/v1/user-info")
    fun getUserInfo(@Header("jwt") jwt : String?) : Call<UserInfoResponse>

    @GET("api/v1/ads")
    fun getAds(@Header("signature") signature : String?) : Call<AdsResponse>

    @GET("api/v1/apps/{appId}/review-feedback")
    fun getReview(@Header("jwt") jwt : String?, @Path("appId") appId : Int?) : Call<ReviewResponse>

    @POST("/api/v1/apps/review")
    fun postReview(@Header("jwt") jwt : String?,
                   @Query("app_id") appId : Int?,
                   @Query("ratings") ratings : Int?,
                   @Query("comment") comment : String?) : Call<BaseResponse>

    @PUT("/api/v1/apps/review")
    fun putReview(@Header("jwt") jwt : String?,
                   @Query("app_id") appId : Int?,
                   @Query("ratings") ratings : Int?,
                   @Query("comment") comment : String?) : Call<BaseResponse>

    @DELETE("/api/v1/apps/review")
    fun deleteReview(@Header("jwt") jwt : String?,
                  @Query("app_id") appId : Int?) : Call<BaseResponse>

    @POST("/api/v1/apps/downloaded")
    fun postStatusDownload(@Header("jwt") jwt : String?, @Query("apps_id") appsId : Int?) : Call<StatusDownloadedResponse>

    @POST("api/v1/apps/installed")
    fun getInstalledApps(@Header("jwt") jwt : String?, @Body data : UpdateRequest) : Call<AppResponse>

}