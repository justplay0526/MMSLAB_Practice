package com.practice.androidquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ImageFragment : Fragment() {

    companion object {
        fun newInstance(imageUrl: String): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putString("image_url", imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = arguments?.getString("image_url")
        //用來載入圖片URL的第三方程式
        Glide.with(this)
            .load(imageUrl)
            .into(view.findViewById(R.id.imageView))
    }
}
