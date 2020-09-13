package id.mjs.etalaseapp.ui.changepassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.repository.UserRepository

class ChangePasswordViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()

    fun getUserInfo(jwt : String) : MutableLiveData<UserInfoResponse> {
        return userRepository.getUserInfo(jwt)
    }

    fun changePassword(jwt : String?, email : String, oldPassword : String, newPassword : String) : MutableLiveData<ForgotPasswordResponse> {
        return userRepository.changePassword(jwt,email,oldPassword,newPassword)
    }

}