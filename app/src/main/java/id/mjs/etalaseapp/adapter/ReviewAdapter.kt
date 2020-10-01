package id.mjs.etalaseapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.response.Review
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.item_list_apps.view.*
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter(private val list : ArrayList<Review>, private val imageDev : String, private val nameDev : String) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

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
                if (review.reply != null && review.reply!!.isNotEmpty()){
                    reply_review.visibility = View.VISIBLE
//                    val picasso2 = Picasso.get()
//                    picasso2.load(Utils.baseUrl+imageDev)
//                        .into(reply_dev_image_view)
                    Log.d("imageDev",imageDev)
                    developer_reply_name.text = nameDev
                    developer_reply_date.text = review.reply_at.toString()
                    developer_reply_detail.text = review.reply.toString()
                }
                else{
                    reply_review.visibility = View.GONE
                }
//                titik_tiga.setOnClickListener {
//                    Log.d("titik_tiga","asup")
//
//                }

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