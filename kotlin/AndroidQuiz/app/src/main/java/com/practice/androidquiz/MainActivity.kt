package com.practice.androidquiz

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.practice.androidquiz.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty() && requestCode == 0){
            for (result in grantResults)
                if (result != PackageManager.PERMISSION_GRANTED)
                    finish()    //若拒絕給予定位權限，則關閉應用程式
            loadMap()           //因允許定位權限，所以正常執行應用程式
        }
    }

    override fun onMapReady(map: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
        } else {
            //顯示出能定位個人位置的按紐
            map.isMyLocationEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(25.035, 121.54), 13f
            ))
            //增加地圖大頭針
            val marker = MarkerOptions()
            marker.position(LatLng(25.033611, 121.565000))
            marker.title("台北101")
            marker.draggable(true)
            map.addMarker(marker)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMap()
    }
    //載入地圖
    private fun loadMap(){
        val map = supportFragmentManager.findFragmentById(R.id.maps)
                as SupportMapFragment
        map.getMapAsync(this)
    }
    //向API丟出請求
    private fun sendRequest(){
        val url = "https://android-quiz-29a4c.web.app/"
        val req = okhttp3.Request.Builder()
            .url(url)
            .build()

        OkHttpClient().newCall(req).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val myObject = Gson().fromJson(json, MyObject::class.java)
                showDialog(myObject)
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(this@MainActivity, "查詢失敗$e", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showDialog(myObject: MyObject){
        val items = arrayOfNulls<String>(myObject.result.content.size)
        myObject.result.content.forEachIndexed { index, data ->
            //items[index] = "地區：${data.sitename}, 狀態：${data.status}"
        }
        runOnUiThread{
            AlertDialog.Builder(this)
                .setTitle("台灣各地空氣品質")
                .setItems(items, null)
                .show()
        }
    }

    class MyObject {
        lateinit var result: Result
        class Result {
            lateinit var content: Array<Content>
            class Content {
                var lat = ""
                var lng = ""
                var name = ""
                var vincinity = ""
                var photo = ""
                lateinit var landscape: Array<LandScape>
                class LandScape {
                    var star = ""
                }

            }
        }
    }
}