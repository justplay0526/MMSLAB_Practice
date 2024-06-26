package com.practice.androidquiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class GridAdapter(private val context: Context, private val imageUrls: Array<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return imageUrls.size
    }
    override fun getItem(position: Int): Any {
        return imageUrls[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val imageView: ImageView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
            imageView = view.findViewById(R.id.imageView)
            view.tag = imageView
        } else {
            view = convertView
            imageView = view.tag as ImageView
        }

        val imageUrl = imageUrls[position]
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)

        return view
    }
}