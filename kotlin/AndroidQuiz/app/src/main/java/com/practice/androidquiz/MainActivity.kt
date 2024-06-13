package com.practice.androidquiz

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.practice.androidquiz.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var maps: GoogleMap
    private lateinit var searchAdapter: ListAdapter
    private lateinit var historyAdapter: ListAdapter
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
            //顯示出能定位個人位置的按紐
            map.isMyLocationEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(25.035, 121.54), 13f
            ))
            map.setOnMarkerClickListener(this)
            maps = map
        }
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        //創建並顯示對話框
        showMarkerDialog(marker)
        return true
    }

    @Suppress("DEPRECATION")
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //設定搜尋欄清除鍵圖示及其大小
        val drawable = resources.getDrawable(R.drawable.edit_cancel)
        drawable.setBounds(0,0,75, 75)
        binding.edSearch.setCompoundDrawables(null,null,drawable,null)

        binding.edSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val clearDrawable: Drawable? = binding.edSearch.compoundDrawables[2] // 獲得右邊的 drawable
                if (clearDrawable != null && event.rawX >= (binding.edSearch.right
                            - binding.edSearch.paddingEnd - clearDrawable.bounds.width())) {
                    binding.edSearch.text.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

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
                val marker = MarkerOptions()
                runOnUiThread {
                    myObject.results.content.forEach { data ->
                        val name = data.name
                        val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE name = ?", arrayOf(name))
                        //檢查是否存在相同紀錄
                        if (cursor.count == 0){
                            dbrw.execSQL("INSERT INTO apiTable(name, vic, lat, lng , read, star) VALUES(?, ?, ?, ?, ?, ?)",
                                arrayOf(data.name, data.vicinity, data.lat.toFloat(), data.lng.toFloat(), 0, data.star))
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

    fun onFragmentDestroyed() {
        Log.d("MainAct", "DetailFragment is destroyed")
        binding.btnSearch.visibility = View.VISIBLE
        binding.btnHistory.visibility = View.VISIBLE
    }

    private fun showSearchDialog(){
        val results = searchByName(binding.edSearch.text.toString())
        if (results.toString() == "[]"){
            Toast.makeText(this@MainActivity, "查無紀錄", Toast.LENGTH_SHORT).show()
        } else {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_search, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setCancelable(true)
            // 建立 AlertDialog
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.round_dialog_bg)
            dialog.show()
//            results.forEach{c ->
//                val name = c["name"] as String
//            }
            //宣告Adapter
            searchAdapter = ListAdapter(this, results)
            val lsvSql = dialogView.findViewById<ListView>(R.id.lsv_sql)
            lsvSql.adapter = searchAdapter
            lsvSql.onItemClickListener = AdapterView.OnItemClickListener{ _, view, _, _ ->
                val text = view.findViewById<TextView>(R.id.tvName).text.toString()
                dbrw.execSQL("UPDATE apiTable SET READ = 1 WHERE name LIKE '${text}'")
                val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE name LIKE ?", arrayOf(text))
                cursor.moveToFirst()
                val locate = text to LatLng(
                        cursor.getDouble(cursor.getColumnIndexOrThrow("lat")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("lng"))
                    )
                cursor.close()
                //將地圖中心點移到點擊之listview的item上
                maps.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    locate.second, 13f
                ))
                //移動完將dialog隱藏
                dialog.dismiss()
            }
        }
    }

    private fun showHistoryDialog(){
        val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE read = 1", null)
        if (cursor.count == 0){
            Toast.makeText(this@MainActivity, "無歷史紀錄", Toast.LENGTH_SHORT).show()
        } else {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_history, null)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setCancelable(true)
            // 建立 AlertDialog
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.round_dialog_bg)
            dialog.show()

            val results = mutableListOf<Map<String, Any>>()
            historyAdapter = ListAdapter(this, results)
            val lsvHistory = dialogView.findViewById<ListView>(R.id.lsv_history)
            lsvHistory.adapter = historyAdapter

            while (cursor.moveToNext()) {
                val result = mutableMapOf<String, Any>()
                result["name"] = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                result["vic"] = cursor.getString(cursor.getColumnIndexOrThrow("vic"))
                results.add(result)
            }
            cursor.close()

            historyAdapter.notifyDataSetChanged()

            dialogView.findViewById<Button>(R.id.btn_clear).setOnClickListener {
                dbrw.execSQL("UPDATE apiTable SET READ = 0")
                Toast.makeText(this@MainActivity, "已清除紀錄", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }
    private fun showMarkerDialog(marker: Marker) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_info, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(true)
        // 建立 AlertDialog
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.round_info_bg)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        tvTitle.text = marker.title
        dialog.show()
        val btnHotel = dialogView.findViewById<Button>(R.id.btn_hotel)
        btnHotel.setOnClickListener {
            val cursor = dbrw.rawQuery("SELECT * FROM apiTable WHERE name LIKE ?", arrayOf(marker.title))
            cursor.moveToFirst()

            val b = Bundle()
            b.putString("name", marker.title)
            b.putString("vic", cursor.getString(cursor.getColumnIndexOrThrow("vic")))
            cursor.close()

            val frag = DetailFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack(null)
                .commit()

            binding.btnSearch.visibility = View.INVISIBLE
            binding.btnHistory.visibility = View.INVISIBLE


            dialog.dismiss()
        }
    }


    private fun searchByName(keyword: String): MutableList<Map<String, Any>> {
        val dbHelper = MyDBHelper(this)
        val db = dbHelper.readableDatabase

        val keywordWithWildcards = "%$keyword%"

        val cursor = db.rawQuery("SELECT * FROM apiTable WHERE name LIKE ?",
            arrayOf(keywordWithWildcards))
        if (cursor.count == 0) {
            return mutableListOf()
        }
        val results = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val result = mutableMapOf<String, Any>()
            result["name"] = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            result["vic"] = cursor.getString(cursor.getColumnIndexOrThrow("vic"))
            results.add(result)
        }
        cursor.close()

        return results
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
                var photo = ""
                var landscape: Array<String> = arrayOf()
                var star = 1
            }
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
            db?.execSQL("CREATE TABLE apiTable(name text PRIMARY KEY,vic text ,lat real,lng real,read integer, star integer)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS apiTable")
            onCreate(db)
        }
    }

}