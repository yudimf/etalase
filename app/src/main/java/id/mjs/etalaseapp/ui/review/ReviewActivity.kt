package id.mjs.etalaseapp.ui.review

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ReviewAdapter
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.model.response.Review
import id.mjs.etalaseapp.ui.download.DownloadActivity
import id.mjs.etalaseapp.ui.download.DownloadViewModel
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.activity_list_app.*
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private var listReview = ArrayList<Review>()
    private lateinit var reviewAdapter : ReviewAdapter

    private lateinit var appModelSelected : AppDataResponse
    private lateinit var viewModel : ReviewViewModel
    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    companion object {
        const val EXTRA_APP_MODEL = "extra_app_model"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        appModelSelected = intent.getParcelableExtra<AppDataResponse>(ReviewActivity.EXTRA_APP_MODEL) as AppDataResponse
        sharedPreferences = getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        initReviewLayout()
        getReview()
        showLoading(true)
    }

    private fun initReviewLayout(){
        reviewAdapter = ReviewAdapter(listReview, appModelSelected.developers?.picture.toString(), appModelSelected.developers?.name.toString())
        rv_all_review.setHasFixedSize(true)
        rv_all_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_all_review.adapter = reviewAdapter

        val picasso = Picasso.get()
        picasso.load(Utils.baseUrl+"apps/"+appModelSelected.app_icon)
            .into(app_icon_review)

        app_name_review.text = appModelSelected.name

        btn_back_review.setOnClickListener {
            finish()
        }
    }

    private fun showLoading(status : Boolean){
        if (status){
            progress_bar_review?.visibility = View.VISIBLE
            rv_all_review?.visibility = View.GONE
        }
        else{
            progress_bar_review?.visibility = View.GONE
            rv_all_review?.visibility = View.VISIBLE
        }
    }

    private fun getReview(){
        if (jwt.isNotEmpty()){
            viewModel.getReview(jwt.toString(), appModelSelected.idApps!!).observe(this, Observer {
                if (it != null){
                    val response = it.reviewDataResponse
                    val data= response?.review
                    if (data != null){
                        listReview.addAll(data)
                    }
                    reviewAdapter.notifyDataSetChanged()
                    showLoading(false)
                }
            })
        }
//        listReview.add(Review(1,1,1,1,"",1,"","","","","","",null))
    }

}