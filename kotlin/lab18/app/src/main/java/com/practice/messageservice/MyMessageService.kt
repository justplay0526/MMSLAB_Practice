package com.practice.messageservice

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyMessageService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("onNewToken", token)
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        msg.data.entries.forEach {
            Log.e("data", "Key:${it.key}, values:${it.value}")
        }
    }
}