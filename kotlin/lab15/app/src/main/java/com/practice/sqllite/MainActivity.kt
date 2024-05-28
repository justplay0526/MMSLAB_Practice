package com.practice.sqllite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.practice.sqllite.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        //宣告Adapter並連結ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        binding.lsvSql.adapter = adapter
        setListener()

    }

    override fun onDestroy() {
        dbrw.close()
        super.onDestroy()
    }
    private fun setListener(){
        val ed_book = binding.edBook
        val ed_price = binding.edPrice

        binding.btnInsert.setOnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1)
                showToast("欄位請勿留空")
            else{
                try {
                    //新增一本書籍記錄在myTable資料表
                    dbrw.execSQL(
                        "INSERT INTO myTable(book, price) VALUES(?,?)",
                        arrayOf(ed_book.text.toString(),
                                ed_price.text.toString())
                    )
                    showToast("新增:${ed_book.text}, 價格:${ed_price.text}")
                    cleanEditText()
                } catch (e: Exception){
                    showToast("新增失敗:${e}")
                }
            }

            binding.btnUpdate.setOnClickListener {
                if (ed_book.length() < 1 || ed_price.length() < 1)
                    showToast("欄位請勿留空")
                else{
                    try {
                        //尋找相同書名的紀錄並更新price欄位的值
                        dbrw.execSQL(
                            "UPDATE myTable SET price = " +
                                    "${ed_price.text} WHERE book LIKE '${ed_book.text}'")
                        showToast("更新:${ed_book.text}, 價格:${ed_price.text}")
                        cleanEditText()
                    } catch (e: Exception){
                        showToast("更新失敗:${e}")
                    }
                }
            }

            binding.btnDelete.setOnClickListener {
                if (ed_book.length() < 1)
                    showToast("書名請勿留空")
                else{
                    try {
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '${ed_book.text}'")
                        showToast("刪除:${ed_book.text}")
                        cleanEditText()
                    } catch (e: Exception){
                        showToast("刪除失敗:${e}")
                    }
                }
            }

            binding.btnQuery.setOnClickListener {
                val queryString = if (ed_book.length() < 1){
                    "SELECT * FROM myTable"
                } else {
                    "SELECT * FROM myTable WHERE book LIKE '${ed_book.text}'"
                }
                Log.e("MainAct", "${queryString}")
                val cursor = dbrw.rawQuery(queryString, null)
                cursor.moveToFirst()
                items.clear()
                showToast("共有${cursor.count}筆資料")
                for (i in 0 until cursor.count){
                    Log.e("MainAct", "${cursor.getString(0)}")
                    items.add("書名:${cursor.getString(0)}\t\t\t\t 價格:${cursor.getInt(1)}")
                    cursor.moveToNext()
                }
                adapter.notifyDataSetChanged()
                cursor.close()
            }
        }
    }
    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun cleanEditText(){
        binding.edBook.setText("")
        binding.edPrice.setText("")
    }
}
class MyDBHelper(
    context: Context,
    name: String = database,
    factory: SQLiteDatabase.CursorFactory ?= null,
    version: Int = v) :SQLiteOpenHelper(context, name, factory, version){
        companion object {
            private const val database = "myDatabase"
            private const val v = 1
        }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE myTable(book text PRIMARY KEY, price integer NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS myTable")
        onCreate(db)
    }
    }