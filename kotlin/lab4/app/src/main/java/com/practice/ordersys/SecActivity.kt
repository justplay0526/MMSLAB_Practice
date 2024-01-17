package com.practice.ordersys

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.practice.ordersys.databinding.ActivityMainBinding
import com.practice.ordersys.databinding.ActivitySecBinding

class SecActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySecBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSend.setOnClickListener{
            if (binding.edDrink.length() < 1){
                Toast.makeText(this, "請輸入飲料名稱", Toast.LENGTH_SHORT).show()
            } else{
                val b = Bundle()
                b.putString("drink", binding.edDrink.text.toString())
                b.putString("sugar", binding.rgSugar.findViewById<RadioButton>(binding.rgSugar.checkedRadioButtonId).text.toString())
                b.putString("ice", binding.rgIce.findViewById<RadioButton>(binding.rgIce.checkedRadioButtonId).text.toString())
                setResult(Activity.RESULT_OK, Intent().putExtras(b))
                finish()
            }
        }
    }
}