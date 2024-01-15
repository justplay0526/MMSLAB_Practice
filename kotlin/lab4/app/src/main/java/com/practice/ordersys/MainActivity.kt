package com.practice.ordersys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.practice.ordersys.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val resultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()){ result ->
                    if(result.resultCode == Activity.RESULT_OK){
                        val myData: Intent? = result.data
                }
            }
        binding.btnChoice.setOnClickListener{
            val intent = Intent(this, SecActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let {
            if (resultCode == Activity.RESULT_OK){
                binding.tvMeal.text =
                    "飲料： ${it.getString("drink")}\n\n" +
                    "甜度： ${it.getString("sugar")}\n\n" +
                    "冰塊： ${it.getString("ice")}"
            }
        }
    }
}