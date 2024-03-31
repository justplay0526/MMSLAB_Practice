package com.practice.mltesting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.practice.mltesting.databinding.ActivityMainBinding
import java.io.IOException

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private var angle = 0f

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK){
            val image = data?.extras?.get("data") ?: return
            val bitmap = image as Bitmap
            val imageView = binding.imgPic
            imageView.setImageBitmap(bitmap)
            recognizeImage(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, 0)
            } catch (e: ActivityNotFoundException){
                Toast.makeText(this,
                    "此裝置無相機應用程式", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRotate.setOnClickListener {
            val imageView = binding.imgPic
            angle += 90f
            imageView.rotation = angle
            val bitmap = imageView.drawToBitmap()
            recognizeImage(bitmap)
        }
    }
    private fun recognizeImage(bitmap : Bitmap){
        try {
            val labeler = ImageLabeling.getClient(
                ImageLabelerOptions.DEFAULT_OPTIONS
            )
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            labeler.process(inputImage)
                .addOnSuccessListener { labels ->
                    val result = arrayListOf<String>()
                    for (label in labels){
                        val text = label.text
                        val confidence = label.confidence
                        result.add("$text, 可信度：$confidence")
                    }
                    val listView = binding.lvListView
                    listView.adapter = ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, result)
                }
                .addOnFailureListener{e ->
                    Toast.makeText(this, "發生錯誤", Toast.LENGTH_SHORT).show()
                }
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}