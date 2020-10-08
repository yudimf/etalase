package id.mjs.etalaseapp.ui.myapps.downloadedapps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.AppDetailResponse
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AppRepository

class DownloadedAppsViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getInstalledApps(jwt : String?, data : UpdateRequest) : MutableLiveData<AppResponse> {
        return appRepository.getInstalledApps(jwt,data)
    }

    fun checkForUpdate(jwt : String?, data : UpdateRequest) : MutableLiveData<AppResponse> {
        return appRepository.getInstalledApps(jwt,data)
    }

    fun postStatusUpload(jwt : String, appsId : Int) : MutableLiveData<AppDetailResponse>{
        return appRepository.postStatusUpload(jwt,appsId)
    }

}