package com.practice.directory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.directory.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MyAdapter
    private val contacts = ArrayList<Contact>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK){
                Log.e("MainActivity", "getBundle")
                val name = it.getString("name") ?: return@let
                Log.e("MainActivity", "getname")
                val phone = it.getString("phone") ?: return@let
                Log.e("MainActivity", "getphone")
                contacts.add(Contact(name, phone))
                Log.e("MainActivity", "add adapter")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recycleView.layoutManager = linearLayoutManager
        adapter = MyAdapter(contacts)
        binding.recycleView.adapter = adapter
        binding.btnAdd.setOnClickListener {
            startActivityForResult(Intent(this, SecActivity::class.java), 1)
        }
    }


}

data class Contact (
    val name:   String,
    val phone:  String
)