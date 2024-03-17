package com.practice.animation

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.practice.animation.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //將動畫的drawable指定為ImageView的背景資源
        binding.imgFrame.setBackgroundResource(R.drawable.loading_animation)
        val animate = binding.imgFrame.background as AnimationDrawable
        binding.btnStart.setOnClickListener {
            animate.start()
        }
        binding.btnStop.setOnClickListener {
            animate.stop()
        }
        binding.btnAlpha.setOnClickListener {
            val anim = AlphaAnimation(
                1.0f,   //起始透明度
                0.2f    //結束透明度
            )
            anim.duration = 1000 //動畫持續1秒
            binding.imgTween.startAnimation(anim) //執行動畫
        }
        binding.btnScale.setOnClickListener {
            val anim = ScaleAnimation(
                1.0f,       //X起始比例
                1.5f,         //X結束比例
                1.0f,       //Y起始比例
                1.5f          //Y結束比例
            )
            anim.duration = 1000
            binding.imgTween.startAnimation(anim)
        }
        binding.btnTranslate.setOnClickListener {
            val anim = TranslateAnimation(
                0f,     //X起點
                100f,     //X終點
                0f,     //Y起點
                -100f     //Y終點
            )
            anim.duration = 1000
            binding.imgTween.startAnimation(anim)
        }
        binding.btnRotate.setOnClickListener {
            val anim = RotateAnimation(
                0f, //起始角度
                360f, //結束角度
                RotateAnimation.RELATIVE_TO_SELF, //X以自身位置旋轉
                0.5f, //X旋轉中心點
                RotateAnimation.RELATIVE_TO_SELF, //Y以自身位置旋轉
                0.5f //Y旋轉中心點
            )
            anim.duration = 1000
            binding.imgTween.startAnimation(anim)
        }
    }
}