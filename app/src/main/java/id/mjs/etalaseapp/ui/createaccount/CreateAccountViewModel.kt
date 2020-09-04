package id.mjs.etalaseapp.ui.createaccount

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.request.LoginRequest
import id.mjs.etalaseapp.model.response.LoginResponse
import id.mjs.etalaseapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository()

    fun register(email : RequestBody,
                 password : RequestBody,
                 name : RequestBody,
                 sdkVersion : RequestBody,
                 birthday : RequestBody,
                 bodyPhoto : MultipartBody.Part?,
                 imei1 : RequestBody,
                 imei2 : RequestBody,
                 deviceBrand : RequestBody,
                 deviceModel : RequestBody) : MutableLiveData<LoginResponse> {
        return userRepository.register(email, password, name, sdkVersion, birthday, bodyPhoto,imei1,imei2,deviceBrand,deviceModel)
    }
}