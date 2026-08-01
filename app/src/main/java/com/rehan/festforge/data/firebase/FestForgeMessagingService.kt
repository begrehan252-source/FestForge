package com.rehan.festforge.data.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FestForgeMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FestForgeFCM", "Refreshed token: $token")
        // Pass token to backend/Firestore if needed
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FestForgeFCM", "From: ${message.from}")
        message.notification?.let {
            Log.d("FestForgeFCM", "Notification Body: ${it.body}")
        }
    }
}
