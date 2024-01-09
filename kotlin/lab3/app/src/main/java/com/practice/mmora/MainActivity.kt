package com.practice.mmora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practice.mmora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnMora.setOnClickListener{
            if (binding.edName.length() < 1) {
                binding.tvText.text = "請輸入姓名以開始遊戲"
                return@setOnClickListener
            }
            val  playerName = binding.edName.text
            val comMora = (Math.random() * 3).toInt()
            val  playerMoraText = when {
                binding.btnScissor.isChecked -> "剪刀"
                binding.btnStone.isChecked -> "石頭"
                else -> "布"
            }
            val  comMoraText = when (comMora) {
                0 -> "剪刀"
                1 -> "石頭"
                else -> "布"
            }
            binding.tvName.text = "名字\n$playerName"
            binding.tvMmora.text = "我方出拳\n$playerMoraText"
            binding.tvCmora.text = "電腦出拳\n$comMoraText"
            when {
                binding.btnScissor.isChecked && comMora == 2 ||
                        binding.btnStone.isChecked && comMora == 0 ||
                        binding.btnPaper.isChecked && comMora == 1 ->   {
                            binding.tvWinner.text = "勝利者\n$playerName"
                            binding.tvText.text = "恭喜你勝利了！！！"
                        }
                binding.btnScissor.isChecked && comMora == 1 ||
                        binding.btnStone.isChecked && comMora == 2 ||
                        binding.btnPaper.isChecked && comMora == 0 ->   {
                    binding.tvWinner.text = "勝利者\n 電腦"
                    binding.tvText.text = "可惜，電腦獲勝了！"
                }
                else -> {
                    binding.tvWinner.text = "勝利者\n 平手"
                    binding.tvText.text = "平局，請再試一次！"
                }
            }
        }
    }
}