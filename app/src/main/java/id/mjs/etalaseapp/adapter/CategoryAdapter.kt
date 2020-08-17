package id.mjs.etalaseapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(private val listCategory: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listCategory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(category: Category){
            with(itemView) {
                txt_category_item.text = category.categoryName
                Log.d("path","https://api-etalase-app.bagustech.id/"+category.categoryImage)
                val picasso = Picasso.get()
                picasso.load("https://api-etalase-app.bagustech.id/"+category.categoryImage)
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(img_category_item)
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(category)
                }
            }
        }
    }
}