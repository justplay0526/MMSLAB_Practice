package com.practice.androidquiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DetailFrag", "OnCreated")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val b = arguments
        if (b != null){
            Log.d("DetailFrag", "notNull")
            val rating = b.getString("star")!!.toInt()
            val photoUrl = b.getString(("photo"))
            val landscapeUrl = b.getString("landscape")
            val urlArray = landscapeUrl!!.split("^").toTypedArray()
            val gridAdapter = GridAdapter(requireContext(), arrayOf(*urlArray))
            val starContainer = view.findViewById<LinearLayout>(R.id.starContainer)
            //載入旅館星級數
            addStars(starContainer, rating)
            //載入詳細資訊頁面的圖片
            Glide.with(this)
                .load(photoUrl)
                .into(view.findViewById(R.id.info_pic))
            view.findViewById<TextView>(R.id.tv_hotelName).text = b.getString("name")
            view.findViewById<TextView>(R.id.tv_hotelVic).text = b.getString("vic")
            view.findViewById<TextView>(R.id.tv_view_pic).text = "景觀圖("+urlArray.size+")"
            val gridView = view.findViewById<GridView>(R.id.gridView)
            gridView.adapter = gridAdapter
            gridView.numColumns = 3 //設定一行能放置的item數
            //設定Item被點擊的Listener
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
                val picFragment = PicFragment().apply {
                    arguments = Bundle().apply {
                        putInt("pos",pos)
                        putStringArray("urlArray",urlArray)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, picFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this).commitNow()
            onDestroy()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("DetailFrag", "onDestroy")
        // 在 Fragment 銷毀時發送一個訊號給 MainActivity
        (activity as? MainActivity)?.onFragmentDestroyed()
    }

    private fun addStars(container: LinearLayout, rating: Int) {
        container.removeAllViews() //清除container之前的View

        val sizeIndp = 150 //限制圖片大小

        for (i in 1..rating) {
            val star = ImageView(context)
            star.setImageResource(R.drawable.viewstar)
            val params = LinearLayout.LayoutParams(
                sizeIndp,
                sizeIndp
            )
            star.layoutParams = params
            container.addView(star)
        }
    }
}