package com.practice.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        Thread{
            try {
                Thread.sleep(3000)
                val intent = Intent(this, SecActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }catch (e: InterruptedException){
                e.printStackTrace()
            }
        }.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY//終止後不在重啟
    }

    override fun onBind(p0: Intent?): IBinder ?= null
}