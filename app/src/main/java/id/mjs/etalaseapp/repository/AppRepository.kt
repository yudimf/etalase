package id.mjs.etalaseapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.*
import id.mjs.etalaseapp.retrofit.ApiMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        ApiMain().services.getCategoriesApp(jwt).enqueue(object : Callback<CategoryResponse>{
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

        ApiMain().services.getCategoriesAnonymousApp(signature).enqueue(object : Callback<CategoryResponse>{
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

    fun getCategoriesGames(jwt: String) : MutableLiveData<CategoryResponse>{
        val categoryResponse = MutableLiveData<CategoryResponse>()

        ApiMain().services.getCategoriesGames(jwt).enqueue(object : Callback<CategoryResponse>{
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

    fun getCategoriesAnomymousGames(signature: String) : MutableLiveData<CategoryResponse>{
        val categoryResponse = MutableLiveData<CategoryResponse>()

        ApiMain().services.getCategoriesAnonymousGames(signature).enqueue(object : Callback<CategoryResponse>{
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
                Log.d("onFailure",t.message.toString())
                reviewResponse.postValue(null)
            }

            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                Log.d("onResponse",response.toString())
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

    fun putReview(jwt : String, appId : Int, ratings : Int, comment : String) : MutableLiveData<BaseResponse>{
        val reviewResponse = MutableLiveData<BaseResponse>()

        ApiMain().services.putReview(jwt,appId,ratings,comment).enqueue(object : Callback<BaseResponse>{
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                reviewResponse.postValue(null)
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful){
                    reviewResponse.postValue(response.body())
                }
                else{
                    reviewResponse.postValue(null)
                }
            }
        })
        return reviewResponse
    }

    fun deleteReview(jwt : String, appId : Int) : MutableLiveData<BaseResponse>{
        val reviewResponse = MutableLiveData<BaseResponse>()

        ApiMain().services.deleteReview(jwt,appId).enqueue(object : Callback<BaseResponse>{
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                reviewResponse.postValue(null)
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful){
                    reviewResponse.postValue(response.body())
                }
                else{
                    reviewResponse.postValue(null)
                }
            }
        })
        return reviewResponse
    }

    fun getInstalledApps(jwt : String?, data : UpdateRequest) : MutableLiveData<AppResponse>{
        val appResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getInstalledApps(jwt,data).enqueue(object : Callback<AppResponse>{
            override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
                appResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppResponse>, response: Response<AppResponse>) {
                if (response.isSuccessful){
                    appResponse.postValue(response.body())
                }
                else{
                    appResponse.postValue(null)
                }
            }

        })

        return appResponse
    }

    fun postStatusDownload(jwt : String, appsId : Int) : MutableLiveData<StatusDownloadedResponse>{
        val responseStatus = MutableLiveData<StatusDownloadedResponse>()

        ApiMain().services.postStatusDownload(jwt,appsId).enqueue(object : Callback<StatusDownloadedResponse>{
            override fun onFailure(call: Call<StatusDownloadedResponse>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
                responseStatus.postValue(null)
            }

            override fun onResponse(call: Call<StatusDownloadedResponse>, response: Response<StatusDownloadedResponse>) {
                if (response.isSuccessful){
                    responseStatus.postValue(response.body())
                }
                else{
                    responseStatus.postValue(null)
                }
            }

        })

        return responseStatus
    }

}