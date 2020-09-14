package id.mjs.etalaseapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.*
import id.mjs.etalaseapp.retrofit.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query

class AppRepository {

    fun getAppByName(jwt : String, appName : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAppsByName(jwt,appName).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }

        })
        return listAppDataResponse
    }

    fun getAppByNameAnonymous(signature : String, appName : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAppsByNameAnonymous(signature,appName).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }
        })
        return listAppDataResponse
    }

    fun getAllAppAnonymous(signature : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAllAppAnonymous(signature).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }
        })
        return listAppDataResponse
    }

    fun getAllApp(jwt : String) : MutableLiveData<AppResponse>{
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAllApp(jwt).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }

        })
        return listAppDataResponse
    }

    fun getCategories(jwt: String) : MutableLiveData<CategoryResponse>{
        val categoryResponse = MutableLiveData<CategoryResponse>()

        ApiMain().services.getCategories(jwt).enqueue(object : Callback<CategoryResponse>{
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful){
                    categoryResponse.postValue(response.body())
                }
            }
        })

        return categoryResponse
    }

    fun getCategoriesAnomymous(signature: String) : MutableLiveData<CategoryResponse>{
        val categoryResponse = MutableLiveData<CategoryResponse>()

        ApiMain().services.getCategoriesAnonymous(signature).enqueue(object : Callback<CategoryResponse>{
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful){
                    categoryResponse.postValue(response.body())
                }
            }
        })

        return categoryResponse
    }

    fun getAppsByCategory(jwt : String, categoryId : Int) : MutableLiveData<AppResponse>{
        val categoryResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAppsByCategory(jwt,categoryId).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    categoryResponse.postValue(response.body())
                }
            }
        })
        return categoryResponse
    }

    fun getAppsByCategoryAnonymous(signature : String, categoryId : Int) : MutableLiveData<AppResponse>{
        val categoryResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getAppsByCategoryAnonymous(signature,categoryId).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    categoryResponse.postValue(response.body())
                }
            }

        })
        return categoryResponse
    }

    fun getReview(jwt: String, appId : Int) : MutableLiveData<ReviewResponse>{
        val reviewResponse = MutableLiveData<ReviewResponse>()

        ApiMain().services.getReview(jwt,appId).enqueue(object : Callback<ReviewResponse>{
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                reviewResponse.postValue(null)
            }

            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful){
                    reviewResponse.postValue(response.body())
                }
            }
        })
        return reviewResponse
    }

    fun postReview(jwt : String, appId : Int, ratings : Int, comment : String) : MutableLiveData<BaseResponse>{
        val reviewResponse = MutableLiveData<BaseResponse>()

        ApiMain().services.postReview(jwt,appId,ratings,comment).enqueue(object : Callback<BaseResponse>{
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                reviewResponse.postValue(null)
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful){
                    reviewResponse.postValue(response.body())
                }
            }
        })

        return reviewResponse
    }

    fun checkForUpdate(jwt : String?, data : UpdateRequest) : MutableLiveData<AppResponse>{
        val appResponse = MutableLiveData<AppResponse>()

        ApiMain().services.checkForUpdate(jwt,data).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
                appResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    appResponse.postValue(response.body())
                }
            }

        })

        return appResponse
    }


}