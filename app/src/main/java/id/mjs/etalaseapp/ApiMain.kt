package id.mjs.etalaseapp

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiMain : Application() {
    private val client
        get() = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://androidwave.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val services: ApiServices = retrofit.create(ApiServices::class.java)
}