package com.practice.androidquiz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListAdapter (private val context: Context, private val places: MutableList<Map<String, Any>>
): BaseAdapter(){
    override fun getCount(): Int {
        return places.size
    }

    override fun getItem(position: Int): Any {
        return places[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag= viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val place = places[position]
        viewHolder.tvName.text = place["name"] as String //將旅館名稱放進tvName
        viewHolder.tvVic.text = place["vic"] as String //將旅館位置放進tvVic

        return view
    }

    private class ViewHolder(view: View) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvVic: TextView = view.findViewById(R.id.tvVic)
    }
}