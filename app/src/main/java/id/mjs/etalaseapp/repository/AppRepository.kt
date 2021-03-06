package id.mjs.etalaseapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateDataRequest
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

    fun getBestSellerGames(jwt : String) : MutableLiveData<AppResponse>{
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getBestSellerGames(jwt).enqueue(object : Callback<AppResponse>{
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

    fun getBestSellerGamesAnonymous(signature : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getBestSellerGamesAnonymous(signature).enqueue(object : Callback<AppResponse>{
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

    fun getPopularGames(jwt : String) : MutableLiveData<AppResponse>{
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getPopularGames(jwt).enqueue(object : Callback<AppResponse>{
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

    fun getPopularGamesAnonymous(signature : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getPopularGamesAnonymous(signature).enqueue(object : Callback<AppResponse>{
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

    fun getPopularApps(jwt : String) : MutableLiveData<AppResponse>{
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getPopularApps(jwt).enqueue(object : Callback<AppResponse>{
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

    fun getPopularAppsAnonymous(signature : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getPopularAppsAnonymous(signature).enqueue(object : Callback<AppResponse>{
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

    fun getBestSellerApps(jwt : String) : MutableLiveData<AppResponse>{
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getBestSellerApps(jwt).enqueue(object : Callback<AppResponse>{
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

    fun getBestSellerAppsAnonymous(signature : String) : MutableLiveData<AppResponse> {
        val listAppDataResponse = MutableLiveData<AppResponse>()

        ApiMain().services.getBestSellerAppsAnonymous(signature).enqueue(object : Callback<AppResponse>{
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

        ApiMain().services.getApp(jwt,categoryId).enqueue(object : Callback<AppResponse>{
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

    fun getDetailApp(jwt: String, appId : Int) : MutableLiveData<AppDetailResponse>{
        val detailResponse = MutableLiveData<AppDetailResponse>()

        ApiMain().services.getDetailApp(jwt,appId).enqueue(object : Callback<AppDetailResponse>{
            override fun onFailure(call: Call<AppDetailResponse>, t: Throwable) {
                detailResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppDetailResponse>, response: Response<AppDetailResponse>) {
                if (response.isSuccessful){
                    detailResponse.postValue(response.body())
                }
                else{
                    detailResponse.postValue(null)
                }
            }

        })

        return detailResponse
    }

    fun getDetailApp2(jwt: String, appId : Int, data : UpdateDataRequest) : MutableLiveData<AppDetailResponse>{
        val detailResponse = MutableLiveData<AppDetailResponse>()

        ApiMain().services.getDetailApp(jwt,appId, data).enqueue(object : Callback<AppDetailResponse>{
            override fun onFailure(call: Call<AppDetailResponse>, t: Throwable) {
                detailResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppDetailResponse>, response: Response<AppDetailResponse>) {
                if (response.isSuccessful){
                    detailResponse.postValue(response.body())
                }
                else{
                    detailResponse.postValue(null)
                }
            }

        })

        return detailResponse
    }

    fun getDetailAppAnonymous(signature: String, appId : Int) : MutableLiveData<AppDetailResponse>{
        val detailResponse = MutableLiveData<AppDetailResponse>()

        ApiMain().services.getDetailAppAnonymous(signature,appId).enqueue(object : Callback<AppDetailResponse>{
            override fun onFailure(call: Call<AppDetailResponse>, t: Throwable) {
                detailResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppDetailResponse>, response: Response<AppDetailResponse>) {
                if (response.isSuccessful){
                    detailResponse.postValue(response.body())
                }
                else{
                    detailResponse.postValue(null)
                }
            }

        })

        return detailResponse
    }

    fun getDetailAppAnonymous2(signature: String, appId : Int, data : UpdateDataRequest) : MutableLiveData<AppDetailResponse>{
        val detailResponse = MutableLiveData<AppDetailResponse>()

        ApiMain().services.getDetailAppAnonymous(signature, appId, data).enqueue(object : Callback<AppDetailResponse>{
            override fun onFailure(call: Call<AppDetailResponse>, t: Throwable) {
                detailResponse.postValue(null)
            }

            override fun onResponse(call: Call<AppDetailResponse>, response: Response<AppDetailResponse>) {
                if (response.isSuccessful){
                    detailResponse.postValue(response.body())
                }
                else{
                    detailResponse.postValue(null)
                }
            }

        })

        return detailResponse
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

    fun postStatusUpload(jwt : String, appsId : Int) : MutableLiveData<AppDetailResponse>{
        val responseStatus = MutableLiveData<AppDetailResponse>()

        ApiMain().services.postStatusUpdate(jwt,appsId).enqueue(object : Callback<AppDetailResponse>{
            override fun onFailure(call: Call<AppDetailResponse>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
                responseStatus.postValue(null)
            }

            override fun onResponse(call: Call<AppDetailResponse>, response: Response<AppDetailResponse>) {
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