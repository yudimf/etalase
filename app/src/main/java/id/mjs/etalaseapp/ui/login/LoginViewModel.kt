package id.mjs.etalaseapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    fun login(data : LoginRequest) : MutableLiveData<LoginResponse> {
        return userRepository.login(data)
    }

    fun login2(email: String,password: String,sdkVersion : String, imei1:String, imei2:String,devicebrand:String,deviceModel:String,firebaseId:String) : MutableLiveData<LoginResponse> {
        return userRepository.login2(email,password,sdkVersion,imei1,imei2,devicebrand,deviceModel,firebaseId)
    }

}