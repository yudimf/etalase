package id.mjs.etalaseapp.adapter

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.squareup.picasso.Picasso
import id.mjs.etalaseapp.R
import kotlinx.android.synthetic.main.layout_media_image.view.*
import kotlinx.android.synthetic.main.layout_media_video.view.*

class AdapterRecyclerView(private val listViewType: List<String>) : RecyclerView.Adapter<AdapterRecyclerView.ViewHolder>() {

    private lateinit var mContext : Context

    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private val mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private val urlList = listOf(mp4Url to "default")

    private lateinit var vhProgressBar : ProgressBar

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(mContext, "exoplayer-sample")
    }

    companion object {
        const val ITEM_A = 1
        const val ITEM_B = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_A -> ViewHolderItemA(inflater.inflate(R.layout.layout_media_video, null))
            else -> ViewHolderItemB(inflater.inflate(R.layout.layout_media_image, null))
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemType(listViewType[position])) {
            ITEM_A -> {
                val viewHolderA = holder as ViewHolderItemA
                viewHolderA.bind(listViewType[position])
            }
            else -> {
                val viewHolderB = holder as ViewHolderItemB
                viewHolderB.bind(listViewType[position])
            }
        }
    }

    private fun getExtension(url: String) : String{
        val fileName: String = url

        val index = fileName.lastIndexOf('.')
        return when {
            index > 0 -> {
                fileName.substring(index + 1)
            }
            else -> {
                ""
            }
        }
    }

    override fun getItemCount(): Int = listViewType.size

    override fun getItemViewType(position: Int): Int{
        return getItemType(listViewType[position])
    }

    private fun getItemType(url: String) : Int{
        return if (getExtension(url) == "mp4"){
            ITEM_A
        } else{
            ITEM_B
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItemA(itemView: View) : ViewHolder(itemView) {
        fun bind(url : String){
            with(itemView){
                vhProgressBar = findViewById(R.id.progressBar)

                simpleExoplayer = SimpleExoPlayer.Builder(context).build()
                exoplayerView.player = simpleExoplayer
                simpleExoplayer.seekTo(playbackPosition)
                simpleExoplayer.playWhenReady = false

                simpleExoplayer.addListener(object : Player.EventListener{

                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playbackState == Player.STATE_BUFFERING)
                            vhProgressBar.visibility = View.VISIBLE
                        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
                            vhProgressBar.visibility = View.INVISIBLE
                    }

                })

                val uri = Uri.parse(url)
                val mediaSource = buildMediaSource(uri, "default")
                simpleExoplayer.prepare(mediaSource)

            }
        }
    }

    inner class ViewHolderItemB(itemView: View) : ViewHolder(itemView){
        fun bind(url : String){
            with(itemView){
                val picasso1 = Picasso.get()
//                picasso1.load(url)
//                picasso1.load("https://raw.githubusercontent.com/yudimf/sample_image/master/1.jpeg")
                picasso1.load("http://api-etalase-app.bagustech.id/media/media_4_1.png")
                    .into(image_media)
            }
        }
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return if (type == "dash") {
            DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }


}