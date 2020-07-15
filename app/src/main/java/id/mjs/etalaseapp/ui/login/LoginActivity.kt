package id.mjs.etalaseapp.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.mjs.etalaseapp.ApiMain
import id.mjs.etalaseapp.MainActivity
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.createaccount.CreateAccountActivity
import id.mjs.etalaseapp.ui.forgotpassword.ForgotPasswordActivity
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_create_account.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        tv_forgot_password.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

//        val fileName = "SampleDownloadApp.apk"
//        var destination =
//            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
//        destination += fileName

//        ApiMain().services.getAllTeam().enqueue(object : Callback<ResponseBody>{
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                val path = destination
//                response.body()?.byteStream()?.saveToFile( path)
//                Log.d("asup",path)
//                val sesuatu = File(path)
//                val updatedApk = File(
//                    path
//                )
//                val intent = Intent(Intent.ACTION_VIEW)
//                intent.setDataAndType(
//                    Uri.fromFile(updatedApk),
//                    "application/vnd.android.package-archive"
//                )
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            }
//
//
//        })

    }

//    private fun InputStream.saveToFile(file: String) = use { input ->
//        File(file).outputStream().use { output ->
//            input.copyTo(output)
//        }
//    }

}