package com.practice.viewpage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SecondFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("SecondFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("SecondFragment", "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    // onActivityCreated is deprecated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("FirstFragment", "onViewCreated")
    }
    override fun onStart() {
        super.onStart()
        Log.e("SecondFragment", "Start")
    }

    override fun onResume() {
        super.onResume()
        Log.e("SecondFragment", "Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("SecondFragment", "Pause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("SecondFragment", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("SecondFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("SecondFragment", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("SecondFragment", "onDetach")
    }
}