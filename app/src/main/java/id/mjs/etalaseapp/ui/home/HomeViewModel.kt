package id.mjs.etalaseapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AdsResponse
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AdsRepository
import id.mjs.etalaseapp.repository.AppRepository

class HomeViewModel (application: Application) : AndroidViewModel(application) {

    private val adsRepository = AdsRepository()
    private val appRepository = AppRepository()

    fun getAds(signature : String) : MutableLiveData<AdsResponse>{
        return adsRepository.getAds(signature)
    }

    fun getAllApp(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getAllApp(jwt)
    }

    fun getAllAppAnonymous(signature: String) : MutableLiveData<AppResponse>{
        return appRepository.getAllAppAnonymous(signature)
    }

    fun getPopularGames(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getPopularGames(jwt)
    }

    fun getPopularGamesAnonymous(signature: String) : MutableLiveData<AppResponse>{
        return appRepository.getPopularGamesAnonymous(signature)
    }

    fun getPopularApps(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getPopularApps(jwt)
    }

    fun getPopularAppsAnonymous(signature: String) : MutableLiveData<AppResponse>{
        return appRepository.getPopularAppsAnonymous(signature)
    }

    fun getBestSellerApps(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getBestSellerApps(jwt)
    }

    fun getBestSellerAppsAnonymous(signature: String) : MutableLiveData<AppResponse>{
        return appRepository.getBestSellerAppsAnonymous(signature)
    }

}