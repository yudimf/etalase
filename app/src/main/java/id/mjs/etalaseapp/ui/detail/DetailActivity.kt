package id.mjs.etalaseapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_download.*

class DetailActivity : AppCompatActivity() {

    lateinit var appModelSelected : AppDataResponse

    companion object {
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        appModelSelected = intent.getParcelableExtra<AppDataResponse>(EXTRA_APP_MODEL) as AppDataResponse

        btn_back_detail.setOnClickListener {
            finish()
        }

        val picasso = Picasso.get()
        picasso.load(Utils.baseUrl+"apps/"+appModelSelected.app_icon).into(app_icon_detail)

        app_name_detail.text = appModelSelected.name
        text_detail.text = appModelSelected.description

    }
}