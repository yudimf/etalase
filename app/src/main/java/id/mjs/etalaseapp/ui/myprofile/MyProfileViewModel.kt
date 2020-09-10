package id.mjs.etalaseapp.ui.myprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.model.response.UserInfoResponse
import id.mjs.etalaseapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MyProfileViewModel (application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()

    fun getUserInfo(jwt : String) : MutableLiveData<UserInfoResponse> {
        return userRepository.getUserInfo(jwt)
    }

    fun updateProfile(jwt : String,
                      email : RequestBody,
                      name : RequestBody,
                      birthday : RequestBody,
                      bodyPhoto : MultipartBody.Part?) : MutableLiveData<LoginResponse>{
        return userRepository.updateProfile(jwt,email,name,birthday,bodyPhoto)
    }

}