package com.practice.weatherapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.practice.weatherapi.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    class MyObject {
            lateinit var records:Array<Record>
            class Record{
                var sitename = ""
                var status = ""
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuery.setOnClickListener {
            binding.btnQuery.isEnabled = false
            sendRequest()
        }
    }

    private fun sendRequest(){
        val url = "https://data.moenv.gov.tw/api/v2/aqx_p_432?language=zh" +
                "&offset=0&limit=100&api_key=3907da74-56c0-4bb9-8637-b45009312b6e"
        val req = Request.Builder()
            .url(url)
            .build()

        OkHttpClient().newCall(req).enqueue(object :Callback{
            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val myObject = Gson().fromJson(json, MyObject::class.java)
                showDialog(myObject)
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    binding.btnQuery.isEnabled = true
                    Toast.makeText(this@MainActivity, "查詢失敗$e", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showDialog(myObject: MyObject){
        val items = arrayOfNulls<String>(myObject.records.size)
        myObject.records.forEachIndexed { index, data ->
            items[index] = "地區：${data.sitename}, 狀態：${data.status}"
        }
        runOnUiThread{
            binding.btnQuery.isEnabled = true
            AlertDialog.Builder(this)
                .setTitle("台灣各地空氣品質")
                .setItems(items, null)
                .show()
        }
    }
}