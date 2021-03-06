package id.mjs.etalaseapp.ui.checkforupdate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.UpdateRequest
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AppRepository

class CheckForUpdateViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun checkForUpdate(jwt : String?, data : UpdateRequest) : MutableLiveData<AppResponse> {
        return appRepository.getInstalledApps(jwt,data)
    }

}