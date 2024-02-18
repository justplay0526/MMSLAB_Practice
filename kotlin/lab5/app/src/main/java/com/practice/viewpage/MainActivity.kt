package com.practice.viewpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practice.viewpage.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("MainActivity","onCreate")
        val adapter = ViewPageAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("MainActivity", "Restart")
    }

    override fun onStart() {
        super.onStart()
        Log.e("MainActivity", "Start")
    }

    override fun onResume() {
        super.onResume()
        Log.e("MainActivity", "Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("MainActivity", "Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity", "onDestroy")
    }
}

class ViewPageAdapter(fm:FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc){
    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> FirstFragment()
        1 -> SecondFragment()
        2 -> ThirdFragment()
        else -> throw IllegalArgumentException("Invalid position")
    }
    override fun getItemCount(): Int = 3
}