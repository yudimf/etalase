package id.mjs.etalaseapp.ui.review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.adapter.ReviewAdapter
import id.mjs.etalaseapp.model.Review
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private var listReview = ArrayList<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        initReviewLayout()
    }

    private fun initReviewLayout(){
        addList()
        val reviewAdapter = ReviewAdapter(listReview)
        rv_all_review.setHasFixedSize(true)
        rv_all_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_all_review.adapter = reviewAdapter
    }

    private fun addList(){
        for (i in 1..10){
            listReview.add(Review(1,"1",1,"1","1"))
        }
    }
}