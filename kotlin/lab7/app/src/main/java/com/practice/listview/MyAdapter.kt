package com.practice.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(context: Context, data: ArrayList<Item>,
                private val layout: Int) :
    ArrayAdapter<Item>(context, layout, data) {

    override fun getView(position: Int, convertView: View?,
                         parent: ViewGroup): View {

        val view = View.inflate(parent.context, layout, null)
        val item = getItem(position) ?: return view
        val img_photo = view.findViewById<ImageView>(R.id.img_photo)
        img_photo.setImageResource(item.photo)
        val tv_msg = view.findViewById<TextView>(R.id.tv_msg)
        tv_msg.text = if (layout == R.layout.adapter_vert)
            item.name
        else
            "${item.name}: ${item.price}元"

        return view
    }
}