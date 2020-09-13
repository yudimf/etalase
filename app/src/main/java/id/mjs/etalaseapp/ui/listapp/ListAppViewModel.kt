package id.mjs.etalaseapp.ui.listapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.repository.AppRepository

class ListAppViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getAppsByCategory(jwt : String, categoryId : Int): MutableLiveData<ListAppDataResponse> {
        return appRepository.getAppsByCategory(jwt,categoryId)
    }

    fun getAppsByCategoryAnonymous(signature : String, categoryId : Int): MutableLiveData<ListAppDataResponse> {
        return appRepository.getAppsByCategoryAnonymous(signature,categoryId)
    }

}