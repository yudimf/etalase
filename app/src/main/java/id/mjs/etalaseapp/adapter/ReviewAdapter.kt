package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Review
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter(private val list : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView){
        fun bind(review: Review){
            with(itemView){
//                reviewImageView.setImageResource(review.reviewImage)
//                reviewerName.text = review.reviewName
//                reviewDate.text = review.reviewDate
//                reviewDetail.text = review.reviewDetail
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}