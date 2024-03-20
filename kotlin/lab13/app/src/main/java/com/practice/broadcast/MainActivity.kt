package com.practice.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practice.broadcast.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            intent.extras?.let {
                binding.tvMsg.text = "${it.getString("msg")}"
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMusic.setOnClickListener {
            register("music")
        }

        binding.btnNews.setOnClickListener {
            register("news")
        }

        binding.btnSport.setOnClickListener {
            register("sport")
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun register(channel: String){
        val intentFilter = IntentFilter(channel)
        registerReceiver(receiver, intentFilter)
        val intent = Intent(this, MyService::class.java)
        startService(intent.putExtra("channel", channel))
    }
}