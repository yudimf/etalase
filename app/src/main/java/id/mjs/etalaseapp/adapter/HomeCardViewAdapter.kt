package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppModel
import id.mjs.etalaseapp.model.response.AppDataResponse
import id.mjs.etalaseapp.utils.Utils
import kotlinx.android.synthetic.main.item_app_cardview.view.*

class HomeCardViewAdapter (private val listAppModel: ArrayList<AppDataResponse>) : RecyclerView.Adapter<HomeCardViewAdapter.CardViewViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AppDataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCardViewAdapter.CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_cardview, parent, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = listAppModel.size

    override fun onBindViewHolder(holder: HomeCardViewAdapter.CardViewViewHolder, position: Int) {
        holder.bind(listAppModel[position])
    }

    inner class CardViewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(appModel: AppDataResponse){
            with(itemView) {
                val picasso = Picasso.get()
                picasso.load(Utils.baseUrl+"apps/"+appModel.app_icon)
                    .into(img_app_card)
                title_app_card.text = appModel.name
                if(appModel.file_size != null){
                    val fileSize = Utils.convertBiteToMB(appModel.file_size!!)
                    val textFileSize = "$fileSize MB"
                    info_app_card.text = textFileSize
                }
                else{
                    info_app_card.visibility = View.INVISIBLE
                }
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appModel)
                }
            }
        }
    }

}