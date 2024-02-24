package com.practice.hintmsg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.practice.hintmsg.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = arrayOf("選項1", "選項2", "選項3", "選項4", "選項5")
        binding.btnToast.setOnClickListener {
            showToast("預設Toast")
        }
        //No longer Custom Toast after android 11 is released.
        binding.btnCustom.setOnClickListener {
            val toast = Toast(this)
            toast.setGravity(Gravity.TOP,0,50)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layoutInflater.inflate(R.layout.custom, null)// Toast.view is deprecated at Android 11
            toast.show()
        }

        binding.btnSnackbar.setOnClickListener {
            Snackbar.make(it, "按鈕式Snackbar", Snackbar.LENGTH_SHORT)
                .setAction("按鈕"){
                    showToast("已回應")
                }.show()
        }

        binding.btnDialog1.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("按鈕式AlertDialog")
                .setMessage("AlertDialog內容")
                .setNeutralButton("左按鈕"){ dialog, which ->
                    showToast("左按鈕")
                }
                .setNegativeButton("中按鈕"){dialog, which ->
                    showToast("中按鈕")
                }
                .setPositiveButton("右按鈕"){dialog, which ->
                    showToast("右按鈕")
                }.show()
        }

        binding.btnDialog2.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("列表式AlertDialog")
                .setItems(item) { dialogInterface, i ->
                    showToast("你選的是${item[i]}")
                }.show()
        }

        binding.btnDialog3.setOnClickListener {
            var position = 0
            AlertDialog.Builder(this)
                .setTitle("單選式AlertDialog")
                .setSingleChoiceItems(item, 0){ dialogInterface, i ->
                    position = i
                }
                .setPositiveButton("確定"){dialog, which ->
                    showToast("你選的是${item[position]}")
                }.show()
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}