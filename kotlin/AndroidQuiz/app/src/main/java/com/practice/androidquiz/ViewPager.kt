package com.practice.androidquiz

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager(fragmentActivity: FragmentActivity, private val imageUrls: List<String>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = imageUrls.size

    override fun createFragment(position: Int): Fragment {
        Log.d("ViewPager", position.toString())
        return ImageFragment.newInstance(imageUrls[position])
    }
}