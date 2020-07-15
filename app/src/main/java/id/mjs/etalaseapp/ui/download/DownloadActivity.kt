package id.mjs.etalaseapp.ui.download

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import id.mjs.etalaseapp.ApiMain
import id.mjs.etalaseapp.R
import kotlinx.android.synthetic.main.activity_download.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val fileName = """${System.currentTimeMillis()}apk"""
        var destination =
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += fileName

        btn_download.setOnClickListener {
            Toast.makeText(this,"Downloading..",Toast.LENGTH_LONG).show()
            ApiMain().services.getAllTeam().enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val path = destination
                    response.body()?.byteStream()?.saveToFile( path)
                    Log.d("asup",path)
                    val sesuatu = File(path)
                    val updatedApk = File(
                        path
                    )
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        Uri.fromFile(updatedApk),
                        "application/vnd.android.package-archive"
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            })
        }

        imageViewDownload.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.meetings")
            startActivity(openURL)
        }
    }

    private fun InputStream.saveToFile(file: String) = use { input ->
        File(file).outputStream().use { output ->
            input.copyTo(output)
        }
    }
}