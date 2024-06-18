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
            initPos = it.getInt("pos", 1) //取得被點擊的圖片序號
            //將所有的圖片URL都抓進來，以便圖片的切換
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
        val adapter = ViewPager(requireActivity(), listOf(*urlArray) ) //設定ViewPager的Adapter
        viewPager.adapter = adapter
        viewPager.setCurrentItem(initPos, false) //設定載入時選擇的圖片
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //每當圖片進行滑動，標題文字也會跟著改變
                view.findViewById<TextView>(R.id.pic_title).text = "(${position +1}/${urlArray.size})"
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            //退回母Fragment的頁面
            parentFragmentManager.popBackStack()
        }
    }

}