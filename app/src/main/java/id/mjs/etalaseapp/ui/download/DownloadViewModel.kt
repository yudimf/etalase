package id.mjs.etalaseapp.ui.download

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.BaseResponse
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.model.response.ReviewResponse
import id.mjs.etalaseapp.repository.AppRepository

class DownloadViewModel (application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getReview(jwt : String, appId : Int) : MutableLiveData<ReviewResponse> {
        return appRepository.getReview(jwt,appId)
    }

    fun getAllAppAnonymous(signature: String) : MutableLiveData<AppResponse>{
        return appRepository.getAllAppAnonymous(signature)
    }

    fun getAllApp(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getAllApp(jwt)
    }

    fun postReview(jwt : String, appId : Int, ratings : Int, comment : String) : MutableLiveData<BaseResponse>{
        return appRepository.postReview(jwt,appId,ratings,comment)
    }

}