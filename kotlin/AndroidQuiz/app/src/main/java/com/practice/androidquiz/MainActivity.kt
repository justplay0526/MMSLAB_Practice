package com.practice.androidquiz

import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var maps: GoogleMap
    private lateinit var apiObj: MyObject
    private var dbItems: ArrayList<String> = ArrayList()
    private lateinit var dbAdapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase
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
            maps = map
            //顯示出能定位個人位置的按紐
            map.isMyLocationEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(25.035, 121.54), 13f
            ))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //設定搜尋欄清除鍵圖示及其大小
        val drawable = resources.getDrawable(R.drawable.edit_cancel)
        drawable.setBounds(0,0,75, 75)
        binding.edSearch.setCompoundDrawables(null,null,drawable,null)
        //載入地圖
        loadMap()
        //接入 API 資料
        sendRequest()
        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        binding.btnSearch.setOnClickListener {
            if (binding.edSearch.text.isEmpty()){
                Toast.makeText(this@MainActivity, "請輸入名稱或地址",Toast.LENGTH_SHORT).show()
            } else{
                showCustomDialog()
            }
        }

        binding.btnHistory.setOnClickListener {
            showHistoryDialog()
        }
    }

    override fun onDestroy() {
        dbrw.close()
        super.onDestroy()
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
                apiObj = myObject
                val marker = MarkerOptions()
                runOnUiThread {
                    myObject.results.content.forEach { data ->
                        marker.position(LatLng(data.lat.toDouble(), data.lng.toDouble()))
                        marker.title(data.name)
                        marker.draggable(true)
                        maps.addMarker(marker)
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread{
                    Toast.makeText(this@MainActivity, "查詢失敗$e", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showCustomDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
        // 建立 AlertDialog
        val dialog = builder.create()
        dialog.show()

        //宣告Adapter
        dbAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,dbItems)
        findViewById<ListView>(R.id.lsv_sql).adapter = dbAdapter
        val queryString = "SELECT * FROM myTable WHERE name LIKE '${apiObj.findContentsByKeyword(
            binding.edSearch.text.toString())}'"
        val c = dbrw.rawQuery(queryString, null)
        if(c.count == 0){
            dbrw.execSQL("INSERT INTO myTable(name, locate) VALUES(?, ?)", arrayOf(apiObj.findContentsByKeyword(
                binding.edSearch.text.toString())))
        }else {
            c.moveToFirst()
            dbItems.clear()
            for (i in 0 until c.count){
                dbItems.add("${c.getString(0)}\n ${c.getString(1)}")
                c.moveToNext()//移動到下一筆
            }
            dbAdapter.notifyDataSetChanged()//更新列表資料
            c.close()//關閉cursor
        }
    }

    private fun showHistoryDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_history, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
        // 建立 AlertDialog
        val dialog = builder.create()
        dialog.show()
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            dbrw.execSQL("DELETE FROM myTable")
        }
    }

    class MyObject {
        lateinit var results: Result
        class Result {
            lateinit var content: Array<Content>
            class Content {
                var lat = ""
                var lng = ""
                var name = ""
                var vincinity = ""
            }
        }
        // 添加方法根據 name 關鍵字查找 Content
        fun findContentsByKeyword(keyword: String): List<Result.Content> {
            return results.content.filter { it.name.contains(keyword, ignoreCase = true) }
        }
    }
    class MyDBHelper(
        context: Context,
        name: String = database,
        factory: CursorFactory ?= null,
        version: Int = v
    ): SQLiteOpenHelper(context, name, factory, version) {
        companion object{
            private const val database = "myDatabase"
            private const val v = 1
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE myTable(name text PRIMARY KEY,locate text NOT NULL)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS myTable")
            onCreate(db)
        }
    }
}