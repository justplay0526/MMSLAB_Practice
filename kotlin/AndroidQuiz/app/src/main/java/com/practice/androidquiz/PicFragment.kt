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
    private lateinit var urlArray:Array<String>
    private var initPos: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PicFrag", "OnCreated")
        arguments?.let {
            initPos = it.getInt("pos", 1)
            urlArray = it.getStringArray("urlArray") ?: emptyArray()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pic, container, false)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        view.findViewById<TextView>(R.id.pic_title).text = "(${initPos + 1}/${urlArray.size})"
        val adapter = ViewPager(requireActivity(), listOf(*urlArray) )
        viewPager.adapter = adapter
        viewPager.setCurrentItem(initPos, false)
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                view.findViewById<TextView>(R.id.pic_title).text = "(${position +1}/${urlArray.size})"
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