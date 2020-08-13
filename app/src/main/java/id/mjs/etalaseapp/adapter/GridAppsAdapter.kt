package id.mjs.etalaseapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.model.AppInfo
import kotlinx.android.synthetic.main.item_grid_apps.view.*

class GridAppsAdapter (private val listAppsModel : ArrayList<AppInfo>) : RecyclerView.Adapter<GridAppsAdapter.ViewHolder>(){

    private var onItemClickCallback : GridAppsAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: GridAppsAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AppInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridAppsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_apps, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listAppsModel.size

    override fun onBindViewHolder(holder: GridAppsAdapter.ViewHolder, position: Int) {
        holder.bind(listAppsModel[position])
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(appInfo: AppInfo){
            with(itemView) {
                img_item_grid.setImageDrawable(appInfo.icon)
                app_name_item_grid.text = appInfo.appName
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appInfo)
                }
            }
        }
    }

}