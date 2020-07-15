package id.mjs.etalaseapp

import id.mjs.etalaseapp.model.TeamResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiServices {

    @POST("source/apk/app-pagination-recyclerview.apk")
    @Streaming
    fun getAllTeam(): Call<ResponseBody>

}