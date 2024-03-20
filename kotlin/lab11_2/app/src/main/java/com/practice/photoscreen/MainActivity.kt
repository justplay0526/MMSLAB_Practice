package com.practice.photoscreen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import com.practice.photoscreen.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var angle = 0f

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK){
            val image = data?.extras?.get("data") ?: return
            binding.imgTitle.setImageBitmap(image as Bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnPhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, 0)// Deprecated
            } catch (e: ActivityNotFoundException){
                Toast.makeText(this, "此裝置無相機應用程式", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRotate.setOnClickListener {
            angle += 90f
            binding.imgTitle.rotation = angle
        }
    }
}