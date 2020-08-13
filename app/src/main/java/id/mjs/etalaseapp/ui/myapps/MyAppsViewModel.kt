package id.mjs.etalaseapp.ui.myapps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyAppsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "testtt"
    }
    val text: LiveData<String> = _text
}