package com.example.videoview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoview.databinding.SubtitleBinding

class SubtitleAdapter(
    private val subtitleObj: ArrayList<SubtitleObj.Result.VideoInfo.CaptionResult.Results.Captions>
) : RecyclerView.Adapter<SubtitleAdapter.ViewHolder>() {

    private var currentPosition = -1

    fun setCurrentPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }

    var onItemClick: (Float) -> Unit = { Log.e("debug", "init") }

    class ViewHolder(
        private val binding: SubtitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubtitleObj.Result.VideoInfo.CaptionResult.Results.Captions,
                 currentPosition: Int,
                 onItemClick: (Float) -> Unit = {}) =
            with(binding) {
                val context = root.context
                tvSubtitle.text = item.content
                tvPos.text = (adapterPosition + 1).toString()

                if (currentPosition == adapterPosition){
                    root.setBackgroundColor(context.getColor(android.R.color.darker_gray))
                } else {
                    root.setBackgroundColor(context.getColor(android.R.color.transparent))
                }

                root.setOnClickListener {
                    onItemClick(item.miniSecond.toFloat())
                }
            }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SubtitleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("RecycleViewAdapter", "進去onViewAttachedToWindow")
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subtitleObj[position], currentPosition, onItemClick)
        Log.d("RecycleViewAdapter", "進去onBindViewHolder: ${position + 1}")
    }
    override fun getItemCount() = subtitleObj.size
}

