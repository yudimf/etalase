package id.mjs.etalaseapp.repository

import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AdsResponse
import id.mjs.etalaseapp.retrofit.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdsRepository {

    fun getAds(signature : String) : MutableLiveData<AdsResponse> {
        val adsResponse = MutableLiveData<AdsResponse>()

        ApiMain().services.getAds(signature).enqueue(object : Callback<AdsResponse>{
            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                adsResponse.postValue(null)
            }

            override fun onResponse(call: Call<AdsResponse>, response: Response<AdsResponse>) {
                if (response.isSuccessful){
                    adsResponse.postValue(response.body())
                }
            }

        })

        return adsResponse
    }

}