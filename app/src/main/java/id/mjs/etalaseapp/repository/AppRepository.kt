package id.mjs.etalaseapp.repository

import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.CategoryResponse
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.model.response.ReviewResponse
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

    fun getAppByNameAnonymous(signature : String, appName : String) : MutableLiveData<ListAppDataResponse> {
        val listAppDataResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAppsByNameAnonymous(signature,appName).enqueue(object : Callback<ListAppDataResponse>{
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

    fun getAllAppAnonymous(signature : String) : MutableLiveData<ListAppDataResponse> {
        val listAppDataResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAllAppAnonymous(signature).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
                if (response.isSuccessful){
                    listAppDataResponse.postValue(response.body())
                }
            }
        })
        return listAppDataResponse
    }

    fun getAllApp(jwt : String) : MutableLiveData<ListAppDataResponse>{
        val listAppDataResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAllApp(jwt).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                listAppDataResponse.postValue(null)
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
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

    fun getAppsByCategory(jwt : String, categoryId : Int) : MutableLiveData<ListAppDataResponse>{
        val categoryResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAppsByCategory(jwt,categoryId).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
                if (response.isSuccessful){
                    categoryResponse.postValue(response.body())
                }
            }
        })
        return categoryResponse
    }

    fun getAppsByCategoryAnonymous(signature : String, categoryId : Int) : MutableLiveData<ListAppDataResponse>{
        val categoryResponse = MutableLiveData<ListAppDataResponse>()

        ApiMain().services.getAppsByCategoryAnonymous(signature,categoryId).enqueue(object : Callback<ListAppDataResponse>{
            override fun onFailure(call: Call<ListAppDataResponse>, t: Throwable) {
                categoryResponse.postValue(null)
            }

            override fun onResponse(call: Call<ListAppDataResponse>, response: Response<ListAppDataResponse>) {
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


}