package com.practice.androidquiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Inflate the layout for this fragment
        Log.d("DetailFrag", "OnCreateView")
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val b = arguments
        if (b != null){
            Log.d("DetailFrag", "notNull")
            val rating = b.getString("star")!!.toInt()
            val photoUrl = b.getString(("photo"))
            val landscapeUrl = b.getString("landscape")
            val urlArray = landscapeUrl!!.split("^").toTypedArray()
            val gridAdapter = GridAdapter(requireContext(), arrayOf(*urlArray))
            Log.d("DetailFrag", photoUrl.toString())
            val starContainer = view.findViewById<LinearLayout>(R.id.starContainer)
            Log.d("DetailFrag", rating.toString())
            addStars(starContainer, rating)
            Glide.with(this)
                .load(photoUrl)
                .into(view.findViewById(R.id.info_pic))
            view.findViewById<TextView>(R.id.tv_hotelName).text = b.getString("name")
            view.findViewById<TextView>(R.id.tv_hotelVic).text = b.getString("vic")
            view.findViewById<TextView>(R.id.tv_view_pic).text = "景觀圖("+urlArray.size+")"
            val gridView = view.findViewById<GridView>(R.id.gridView)
            gridView.adapter = gridAdapter
            gridView.numColumns = 3
        }
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
        container.removeAllViews()

        val sizeIndp = 150

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