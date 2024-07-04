package com.practice.directory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practice.directory.databinding.AdapterRowBinding

class MyAdapter(
    private val data: ArrayList<Contact>,
    private val onClick: OnClick
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface OnClick {
        fun onRemove(item: Contact)
        fun onLongClick(item: Contact) {}
    }

    class ViewHolder(
        private val binding: AdapterRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Contact,
            onClick: OnClick
        ) = with(binding) {
            tvName.text = item.name
            tvPhone.text = item.phone
            imgDel.setOnClickListener {
                onClick.onRemove(item)
            }
            root.setOnLongClickListener {
                onClick.onLongClick(item)
                true
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterRowBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onClick)
    }
}