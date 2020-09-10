package id.mjs.etalaseapp.repository

import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository {
    fun getAppByName(jwt : String, appName : String) : MutableLiveData<ListAppDataResponse> {
        val listAppDataResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAppsByName(jwt,appName).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse( call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }

        })

        return listAppDataResponse
    }
}