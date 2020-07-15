package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppModel

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
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appModel)
                }
            }
        }
    }
}