package id.mjs.etalaseapp.ui.listapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AppRepository

class ListAppViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getAppsByCategory(jwt : String, categoryId : Int): MutableLiveData<AppResponse> {
        return appRepository.getAppsByCategory(jwt,categoryId)
    }

    fun getAppsByCategoryAnonymous(signature : String, categoryId : Int): MutableLiveData<AppResponse> {
        return appRepository.getAppsByCategoryAnonymous(signature,categoryId)
    }

}