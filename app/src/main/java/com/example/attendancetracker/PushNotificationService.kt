package com.example.attendancetracker

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
       sendRegistrationToServer(token)
    }
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }
}