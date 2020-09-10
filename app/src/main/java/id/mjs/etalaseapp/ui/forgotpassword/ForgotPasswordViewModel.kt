package id.mjs.etalaseapp.ui.forgotpassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.ForgotPasswordResponse
import id.mjs.etalaseapp.repository.UserRepository

class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    fun forgotPassword(email : String) : MutableLiveData<ForgotPasswordResponse> {
        return userRepository.forgotPassword(email)
    }

}