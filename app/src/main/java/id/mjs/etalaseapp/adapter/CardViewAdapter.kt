package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.item_app_cardview.view.*
import kotlinx.android.synthetic.main.item_category.view.*

class CardViewAdapter(private val listAppModel: ArrayList<AppModel>) : RecyclerView.Adapter<CardViewAdapter.CardViewViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AppModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewAdapter.CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_cardview, parent, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = listAppModel.size

    override fun onBindViewHolder(holder: CardViewAdapter.CardViewViewHolder, position: Int) {
        holder.bind(listAppModel[position])
    }

    inner class CardViewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(appModel: AppModel){
            with(itemView) {
                val picasso = Picasso.get()
                picasso.load(Utils.baseUrl+"apps/"+appModel.photoPath)
                    .into(img_app_card)
                title_app_card.text = appModel.name
                val fileSize = "${appModel.file_size} MB"
                info_app_card.text = fileSize
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appModel)
                }
            }
        }
    }
}