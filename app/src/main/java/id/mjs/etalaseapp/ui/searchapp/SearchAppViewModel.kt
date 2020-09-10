package id.mjs.etalaseapp.ui.searchapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.response.ListAppDataResponse
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.repository.AppRepository

class SearchAppViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository()

    fun searchAppByName(jwt : String, appName : String) : MutableLiveData<ListAppDataResponse> {
        return appRepository.getAppByName(jwt,appName)
    }

}