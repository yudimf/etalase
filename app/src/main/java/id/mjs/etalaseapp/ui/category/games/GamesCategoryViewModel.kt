package id.mjs.etalaseapp.ui.category.games

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.CategoryResponse
import id.mjs.etalaseapp.repository.AppRepository

class GamesCategoryViewModel (application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository()

    fun getCategories(jwt: String) : MutableLiveData<CategoryResponse> {
        return appRepository.getCategoriesGames(jwt)
    }

    fun getCategoriesAnonymous(signature: String) : MutableLiveData<CategoryResponse> {
        return appRepository.getCategoriesAnomymousGames(signature)
    }

}