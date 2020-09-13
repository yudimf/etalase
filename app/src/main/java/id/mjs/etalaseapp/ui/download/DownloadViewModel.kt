package id.mjs.etalaseapp.ui.download

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.ReviewResponse
import id.mjs.etalaseapp.repository.AppRepository

class DownloadViewModel (application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getReview(jwt : String, appId : Int) : MutableLiveData<ReviewResponse> {
        return appRepository.getReview(jwt,appId)
    }

}