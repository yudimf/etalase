package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.Review
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.item_list_apps.view.*
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter(private val list : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var onItemClickCallback : AppsAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: AppsAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(review: Review){
            with(itemView){
                val picasso = Picasso.get()
                picasso.load(Utils.baseUrl+review.endusers?.picture)
                    .into(review_image_view)
                reviewer_name.text = review.endusers?.name
                review_rating_bar.rating = review.ratings?.toFloat()!!
                review_date.text = review.comment_at
                review_detail.text = review.comment
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