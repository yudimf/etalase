package id.mjs.etalaseapp.ui.home.games

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import id.mjs.etalaseapp.model.response.AdsResponse
import id.mjs.etalaseapp.model.response.AppResponse
import id.mjs.etalaseapp.repository.AdsRepository
import id.mjs.etalaseapp.repository.AppRepository

class GamesHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val adsRepository = AdsRepository()
    private val appRepository = AppRepository()

    fun getAds(signature : String) : MutableLiveData<AdsResponse> {
        return adsRepository.getAds(signature)
    }

    fun getBestSellerGames(jwt : String) : MutableLiveData<AppResponse> {
        return appRepository.getBestSellerGames(jwt)
    }

    fun getBestSellerAnonymous(signature: String) : MutableLiveData<AppResponse> {
        return appRepository.getBestSellerGamesAnonymous(signature)
    }

    fun getPopularGames(jwt : String) : MutableLiveData<AppResponse> {
        return appRepository.getPopularGames(jwt)
    }

    fun getPopularAnonymous(signature: String) : MutableLiveData<AppResponse> {
        return appRepository.getPopularGamesAnonymous(signature)
    }



}