package id.mjs.etalaseapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ApkInstalledReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context,"App Installed",Toast.LENGTH_LONG).show()
        Log.d("ApkInstalledReceiver","onReceive")
    }
}
