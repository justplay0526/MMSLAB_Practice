package com.practice.directory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.directory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MyAdapter
    private val contacts = ArrayList<Contact>()

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                Log.e("MainActivity", "getBundle")
                val name = it.getStringExtra("name") ?: return@let
                Log.e("MainActivity", "getname")
                val phone = it.getStringExtra("phone") ?: return@let
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
        adapter = MyAdapter(contacts, object : MyAdapter.OnClick {
            override fun onRemove(item: Contact) {
                contacts.remove(item)
                adapter.notifyDataSetChanged()
            }
        })
        binding.recycleView.adapter = adapter
        binding.btnAdd.setOnClickListener {
//            startActivityForResult(Intent(this, SecActivity::class.java), 1)
            startForResult.launch(Intent(this, SecActivity::class.java))
        }
    }
}

