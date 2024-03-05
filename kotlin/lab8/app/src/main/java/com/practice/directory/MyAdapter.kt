package com.practice.directory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: ArrayList<Contact>):
    RecyclerView.Adapter<MyAdapter.ViewHolder>(){
    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        val tv_name = v.findViewById<TextView>(R.id.tv_name)
        val tv_phone = v.findViewById<TextView>(R.id.tv_phone)
        val img_del = v.findViewById<ImageView>(R.id.img_del)
    }

    override fun getItemCount() = data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_row, parent, false)
            return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name.text = data[position].name
        holder.tv_phone.text = data[position].phone
        holder.img_del.setOnClickListener{
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }
}