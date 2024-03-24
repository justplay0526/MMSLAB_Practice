package com.practice.resolver

import android.R
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.practice.resolver.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    //定義Provider的Uri
    private val uri = Uri.parse("content://com.practice.lab16")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("Test", uri.toString())
        //宣告Adapter並連結ListView
        adapter = ArrayAdapter(this, R.layout.simple_list_item_1, items)
        binding.lsvSql.adapter = adapter
        setListener()
    }
    private fun setListener(){
        val name = binding.edBook.text
        val price = binding.edPrice.text

        binding.btnInsert.setOnClickListener {
            if (name.isEmpty() || price.isEmpty()){
                showToast("欄位請勿留空")
            }
            else{
                val values = ContentValues()
                values.put("book", name.toString())
                values.put("price", price.toString())
                val contentUri = contentResolver.insert(uri, values)
                if (contentUri != null){
                    showToast("新增:${name}, 價格:${price}")
                    cleanEditText()
                } else{
                    showToast("新增失敗")
                }
            }

        binding.btnUpdate.setOnClickListener {
            val name = binding.edBook.text
            val price = binding.edPrice.text
            if (name.isEmpty() || price.isEmpty()) showToast("欄位請勿留空")
            else{
                val values = ContentValues()
                values.put("price", price.toString())
                val cnt = contentResolver.update(uri, values, name.toString(), null)
                if (cnt > 0){
                    showToast("更新:${name}, 價格:${price}")
                    cleanEditText()
                } else{
                    showToast("更新失敗")
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val name = binding.edBook.text
            if (name.isEmpty() )
                showToast("書名請勿留空")
            else{
                val cnt = contentResolver.delete(uri, name.toString(), null)
                if (cnt > 0){
                    showToast("刪除:${name}")
                    cleanEditText()
                } else {
                    showToast("刪除失敗")
                }
            }
        }

        binding.btnQuery.setOnClickListener {
            val name = binding.edBook.text
            val selection  = if(name.isEmpty()) null else name.toString()
            val cursor = contentResolver.query(uri, null, selection, null, null)
            cursor ?: return@setOnClickListener
            cursor.moveToFirst()
            items.clear()
            showToast("共有${cursor.count}筆資料")
            for (i in 0 until cursor.count){
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