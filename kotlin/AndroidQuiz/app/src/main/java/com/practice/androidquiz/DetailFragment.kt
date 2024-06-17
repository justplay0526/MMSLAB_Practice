package com.practice.androidquiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

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
            val starContainer = view.findViewById<LinearLayout>(R.id.starContainer)
            Log.d("DetailFrag", rating.toString())
            addStars(starContainer, rating)
            view.findViewById<TextView>(R.id.tv_hotelName).text = b.getString("name")
            view.findViewById<TextView>(R.id.tv_hotelVic).text = b.getString("vic")
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