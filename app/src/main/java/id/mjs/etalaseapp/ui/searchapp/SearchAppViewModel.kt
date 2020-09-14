package id.mjs.etalaseapp.ui.searchapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AppRepository

class SearchAppViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository()

    fun searchAppByName(jwt : String, appName : String) : MutableLiveData<AppResponse> {
        return appRepository.getAppByName(jwt,appName)
    }

    fun searchAppByNameAnonymous(signature : String, appName : String) : MutableLiveData<AppResponse> {
        return appRepository.getAppByNameAnonymous(signature,appName)
    }

}