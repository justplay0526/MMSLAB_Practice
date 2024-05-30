package com.practice.androidquiz

import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
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
    private var searchItems: ArrayList<String> = ArrayList()
    private var historyItems: ArrayList<String> = ArrayList()
    private lateinit var apiObj: MyObject
    private lateinit var searchAdapter: ArrayAdapter<String>
    private lateinit var historyAdapter: ArrayAdapter<String>
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
        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        //接入 API 資料
        sendRequest()
        binding.btnSearch.setOnClickListener {
            if (binding.edSearch.text.isEmpty()){
                Toast.makeText(this@MainActivity, "請輸入名稱或地址",Toast.LENGTH_SHORT).show()
            } else{
                showSearchDialog()
            }
        }

        binding.btnHistory.setOnClickListener {
            showHistoryDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
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
        Log.d("MainAct","get API")
        OkHttpClient().newCall(req).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val myObject = Gson().fromJson(json, MyObject::class.java)
                apiObj = myObject
                val marker = MarkerOptions()
                runOnUiThread {
                    myObject.results.content.forEach { data ->
                        val name = data.name
                        val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE name = ?", arrayOf(name))
                        //檢查是否存在相同紀錄
                        if (cursor.count == 0){
                            dbrw.execSQL("INSERT INTO apiTable(name, vic, lat, lng ,read) VALUES(?, ?, ?, ?, ?)",
                                arrayOf(data.name, data.vicinity, data.lat.toFloat(), data.lng.toFloat(), 0))
                        }
                        //關閉指標
                        cursor.close()
                        //將 marker 標註在地圖上
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

    private fun showSearchDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
        // 建立 AlertDialog
        val dialog = builder.create()
        dialog.show()

        //宣告Adapter
        searchAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,searchItems)
        val lsvSql = dialogView.findViewById<ListView>(R.id.lsv_sql)
        lsvSql.adapter = searchAdapter
        searchItems.clear()
        val (names, vicinitys) = apiObj.findContentsByKeyword(binding.edSearch.text.toString())
        for (i in names.indices){
            val queryString = "SELECT * FROM apiTable WHERE name LIKE ?"
            dbrw.rawQuery(queryString, arrayOf(names[i])).use { c ->
                if (c.moveToFirst()) {
                    do {
                        searchItems.add("${c.getString(0)}\t${c.getString(1)}")
                        dbrw.execSQL("UPDATE apiTable SET READ = 1 WHERE name LIKE '${names[i]}'")
                    } while (c.moveToNext())
                } else {
                    dbrw.execSQL("UPDATE apiTable SET READ = 1 WHERE name LIKE '${names[i]}'")
                }
                c.close()
            }
        }
        searchAdapter.notifyDataSetChanged()//更新列表資料

    }

    private fun showHistoryDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_history, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
        // 建立 AlertDialog
        val dialog = builder.create()
        dialog.show()

        historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,historyItems)
        val lsvHistory = dialogView.findViewById<ListView>(R.id.lsv_history)
        historyItems.clear()
        lsvHistory.adapter = historyAdapter
        val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE read = 1", null)
        cursor.moveToFirst()
        for (i in 0 until cursor.count){
            Log.d("MainAct","${cursor.getString(0)}&${cursor.getString(1)}")
            historyItems.add("${cursor.getString(0)}\t${cursor.getString(1)}")
            cursor.moveToNext()
        }
        historyAdapter.notifyDataSetChanged()
        cursor.close()

        dialogView.findViewById<Button>(R.id.btn_clear).setOnClickListener {
            dbrw.execSQL("DELETE FROM apiTable")
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
                var vicinity = ""
            }
        }
        // 添加方法根據 name 關鍵字查找 Content
        fun findContentsByKeyword(keyword: String): Pair<List<String>, List<String>> {
            val filterContents = results.content.filter { it.name.contains(keyword, ignoreCase = true) }
            val names = filterContents.map { it.name }
            val vicinitys = filterContents.map { it.vicinity }
            return Pair(names, vicinitys)
        }
    }
    class MyDBHelper(
        context: Context,
        name: String = database,
        factory: CursorFactory ?= null,
        version: Int = v
    ): SQLiteOpenHelper(context, name, factory, version) {
        companion object{
            private const val database = "apiDATABASE"
            private const val v = 1
        }

        override fun onCreate(db: SQLiteDatabase?) {
            Log.d("DATABASE", "CREATED")
            db?.execSQL("CREATE TABLE apiTable(name text PRIMARY KEY,vic text ,lat real,lng real,read integer)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS apiTable")
            onCreate(db)
        }
    }
}