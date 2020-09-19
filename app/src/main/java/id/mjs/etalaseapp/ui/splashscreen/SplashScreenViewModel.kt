package id.mjs.etalaseapp.ui.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.repository.UserRepository

class SplashScreenViewModel (application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    fun getUserInfo(jwt : String) : MutableLiveData<UserInfoResponse> {
        return userRepository.getUserInfo(jwt)
    }

}