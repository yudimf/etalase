package id.mjs.etalaseapp.ui.category.apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.CategoryResponse
import id.mjs.etalaseapp.repository.AppRepository

class AppCategoryViewModel (application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository()

    fun getCategories(jwt: String) : MutableLiveData<CategoryResponse> {
        return appRepository.getCategories(jwt)
    }

    fun getCategoriesAnonymous(signature: String) : MutableLiveData<CategoryResponse> {
        return appRepository.getCategoriesAnomymous(signature)
    }
}