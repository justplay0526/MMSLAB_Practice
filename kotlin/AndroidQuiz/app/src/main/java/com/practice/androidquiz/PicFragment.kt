package com.practice.androidquiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

class PicFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PicFrag", "OnCreated")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pic, container, false)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val b = arguments
        val array = b!!.getString("array")
        val imageUrls = array!!.split("^").toTypedArray()
        val pos = b.getString("pos")
        val size = b.getString("size")
        view.findViewById<TextView>(R.id.pic_title).text = "(${pos}/${size})"
        val adapter = ViewPager(requireActivity(), listOf(*imageUrls))
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                view.findViewById<TextView>(R.id.pic_title).text = "(${position +1}/${size})"
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}