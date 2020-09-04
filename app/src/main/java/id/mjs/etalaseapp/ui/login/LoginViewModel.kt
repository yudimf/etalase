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

}