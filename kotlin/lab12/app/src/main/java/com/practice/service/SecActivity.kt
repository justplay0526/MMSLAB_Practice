package com.practice.service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practice.service.databinding.ActivitySecBinding

private lateinit var binding: ActivitySecBinding
class SecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}