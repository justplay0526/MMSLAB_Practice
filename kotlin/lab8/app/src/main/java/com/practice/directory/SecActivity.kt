package com.practice.directory

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.practice.directory.databinding.ActivitySecBinding


class SecActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            when {
                binding.edName.length() < 1 -> showToast("請輸入姓名")
                binding.edPhone.length() < 1 -> showToast("請輸入電話")
                else -> {
                    val b = Bundle()
                    b.putString("name", binding.edName.text.toString())
                    b.putString("phone", binding.edPhone.text.toString())
                    setResult(Activity.RESULT_OK, Intent().putExtras(b))
                    Log.e("Testing", "finish trans")
                    finish()
                }
            }
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}