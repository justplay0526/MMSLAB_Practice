package com.practice.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.practice.coroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCalc.setOnClickListener {
            when{
                binding.edHeight.length() < 1 -> showToast("請輸入身高")
                binding.edWeight.length() < 1 -> showToast("請輸入體重")
                binding.edAge.length() < 1 -> showToast("請輸入年齡")
                else -> runCoroutines()
            }
        }
    }

    private fun  runCoroutines() {
        binding.tvSdWeight.text = "標準體重\n無"
        binding.tvFat.text = "體脂肪\n無"
        binding.tvBmi.text = "BMI\n無"
        binding.progressBar.progress = 0
        binding.tvProgress.text = "0%"
        binding.llProgress.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            var progress = 0
            while (progress < 100){
                delay(50)
                binding.progressBar.progress = progress
                binding.tvProgress.text = "$progress%"
                progress++
            }
            binding.llProgress.visibility = View.GONE

            val height  = binding.edHeight.text.toString().toDouble()
            val weight  = binding.edWeight.text.toString().toDouble()
            val age     = binding.edAge.text.toString().toDouble()
            val bmi     = weight / ((height / 100).pow(2))
            val (stand_weight, body_fat) = if (binding.btnBoy.isChecked){
                Pair((height - 80) * 0.7, 1.39 * bmi + 0.16 * age - 19.34)
            } else{
                Pair((height - 70) * 0.6, 1.39 * bmi + 0.16 * age - 9)
            }
            binding.tvSdWeight.text = "標準體重 \n${String.format("%.2f", stand_weight)}"
            binding.tvFat.text = "體脂肪 \n${String.format("%.2f", body_fat)}"
            binding.tvBmi.text = "BMI \n${String.format("%.2f", bmi)}"
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}