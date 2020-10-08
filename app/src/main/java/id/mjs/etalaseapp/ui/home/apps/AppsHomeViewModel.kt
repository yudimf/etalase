package id.mjs.etalaseapp.ui.home.apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AdsResponse
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AdsRepository
import id.mjs.etalaseapp.repository.AppRepository

class AppsHomeViewModel (application: Application) : AndroidViewModel(application) {

    private val adsRepository = AdsRepository()
    private val appRepository = AppRepository()

    fun getAds(signature : String) : MutableLiveData<AdsResponse>{
        return adsRepository.getAds(signature)
    }

    fun getBestSellerGames(jwt : String) : MutableLiveData<AppResponse>{
        return appRepository.getBestSellerGames(jwt)
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