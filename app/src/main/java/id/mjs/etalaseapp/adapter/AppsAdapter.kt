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
import kotlinx.android.synthetic.main.item_list_apps.view.*

class AppsAdapter(private val listAppsModel : ArrayList<AppDataResponse>) : RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(appModel: AppDataResponse){
            with(itemView) {
                val picasso = Picasso.get()
                picasso.load(Utils.baseUrl+"apps/"+appModel.app_icon)
                    .into(img_list_app_item)
                txt_list_app_item.text = appModel.name
                txt_list_rating_item.text = appModel.rate + " "
                if (appModel.file_size != null){
                    val fileSize = Utils.convertBiteToMB(appModel.file_size!!)
                    val textFileSize = "$fileSize MB"
                    txt_list_size_item.text = textFileSize
                }
                else{
                    txt_list_size_item.visibility = View.GONE
                }
                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(appModel)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AppDataResponse)
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