package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppModel
import kotlinx.android.synthetic.main.item_list_apps.view.*

class ListAppsAdapter (private val listAppsModel : ArrayList<AppModel>) : RecyclerView.Adapter<ListAppsAdapter.ViewHolder>(){

    private var onItemClickCallback : ListAppsAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: ListAppsAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(appModel: AppModel){
            with(itemView) {
                img_list_app_item.setImageResource(appModel.photo)
                txt_list_app_item.text = appModel.name
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appModel)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AppModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_apps, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listAppsModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAppsModel[position])
    }
}