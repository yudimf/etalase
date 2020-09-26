package id.mjs.etalaseapp.utils

object Utils {
//    const val baseUrl : String = "http://api.etalase.web.id/"
    const val baseUrl : String = "https://api-etalase-app.bagustech.id/"
    const val signature : String = "hfauef874h2bfjb2ufh2b"

    fun convertBiteToMB(size : Int) : Int{
        var temp : Float = size.toFloat()

        temp = temp / 1024 / 1024
        return temp.toInt()
    }
}