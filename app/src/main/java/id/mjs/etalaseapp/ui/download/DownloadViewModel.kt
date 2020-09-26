package id.mjs.etalaseapp.ui.download

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateDataRequest
import id.mjs.etalaseapp.model.response.*
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

    fun updateReview(jwt : String, appId : Int, ratings : Int, comment : String) : MutableLiveData<BaseResponse>{
        return appRepository.putReview(jwt,appId,ratings,comment)
    }

    fun deleteReview(jwt : String, appId : Int) : MutableLiveData<BaseResponse>{
        return appRepository.deleteReview(jwt,appId)
    }

    fun postStatusDownload(jwt : String, appsId : Int) : MutableLiveData<StatusDownloadedResponse>{
        return appRepository.postStatusDownload(jwt,appsId)
    }

    fun getDetailApp(jwt: String, appId : Int) : MutableLiveData<AppDetailResponse>{
        return appRepository.getDetailApp(jwt,appId)
    }

    fun getDetailAppAnonymous(signature: String, appId : Int) : MutableLiveData<AppDetailResponse>{
        return appRepository.getDetailAppAnonymous(signature,appId)
    }

    fun getDetailApp2(jwt: String, appId : Int, data : UpdateDataRequest) : MutableLiveData<AppDetailResponse>{
        return appRepository.getDetailApp2(jwt,appId,data)
    }

    fun getDetailAppAnonymous2(signature: String, appId : Int, data : UpdateDataRequest) : MutableLiveData<AppDetailResponse>{
        return appRepository.getDetailAppAnonymous2(signature,appId,data)
    }

    fun getAppsByCategory(jwt : String, categoryId : Int) : MutableLiveData<AppResponse>{
        return appRepository.getAppsByCategory(jwt,categoryId)
    }

    fun getAppsByCategoryAnonymous(signature : String, categoryId : Int) : MutableLiveData<AppResponse>{
        return appRepository.getAppsByCategoryAnonymous(signature,categoryId)
    }

}