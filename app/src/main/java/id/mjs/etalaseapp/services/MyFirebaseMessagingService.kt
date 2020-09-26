package id.mjs.etalaseapp.services

import com.google.firebase.messaging.FirebaseMessagingService
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.RemoteMessage
import id.mjs.etalaseapp.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }
}